package com.study.nio.ch;

import com.study.nio.buf.SpringTest;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @Author liaozhenglong
 * @Date 2019/12/30 15:47
 **/
public class SoketNIO extends SpringTest {
    //socket 的三个组件
    /**
     * 1 channel
     * 2 buffer
     * 3 selector*/
    @Test
    public void client(){

        try {
            //1 获取网络通道
            SocketChannel client = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
            //1.1 获取读取本地文件通道
            FileChannel fileChannel = FileChannel.open(Paths.get("2.jpg"), StandardOpenOption.READ);
            //2 获取缓冲区buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            //3 读取并发送数据
            while (fileChannel.read(buffer) !=-1){
                buffer.flip();
                client.write(buffer);
                buffer.clear();
            }
            client.shutdownOutput(); // 关闭输出流，否则服务端一直阻塞等待客户端发送消息
            int len =0;
            while ((len=client.read(buffer)) !=-1){
                System.out.println("接受服务端消息...");
                buffer.flip();
                System.out.println("服务端说："+new String(buffer.array(),0,len));
                buffer.clear();
            }
            client.close();
            fileChannel.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void server(){
        try {
            //1 创建服务端通道
            ServerSocketChannel server = ServerSocketChannel.open();
            //绑定端口
            server.bind(new InetSocketAddress(9898));
            System.out.println("服务端已建立");
            //2 创建缓冲区buffer
            ByteBuffer buf = ByteBuffer.allocate(1024);
            //3 等待连接客户端
            SocketChannel client = server.accept();
            System.out.println("有一个客户端连接...");
            //4 创建本地文件通道
            FileChannel file = FileChannel.open(Paths.get("1230.jpg"), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
            while (client.read(buf)!=-1){
                buf.flip();
                file.write(buf);
                buf.clear();
            }
            System.out.println("服务端保存资源完毕");
            buf.clear();
            buf.put("已经完成数据保存".getBytes());
            buf.flip();
            client.write(buf);
            file.close();
            client.close();
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
