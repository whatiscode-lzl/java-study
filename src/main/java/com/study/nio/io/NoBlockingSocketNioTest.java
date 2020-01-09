package com.study.nio.io;

import com.study.nio.buf.SpringTest;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @Author liaozhenglong
 * @Date 2019/12/30 17:34
 **/
public class NoBlockingSocketNioTest extends SpringTest {

    public static void main(String[] args){
       /* Scanner input = new Scanner(System.in);
        System.out.println("请输入一个字符串(中间能加空格或符号)");
        String line = input.nextLine();
        String s = LocalDateTime.now().toString() + "\n" + line;
        System.out.println(s);*/
       client();
    }

    @Test
    public void testScanner(){
        Scanner input = new Scanner(System.in);
        System.out.println("请输入一个字符串(中间能加空格或符号)");
        String line = input.nextLine();
        String s = LocalDateTime.now().toString() + "\n" + line;
        System.out.println(s);
    }

    public static void client(){
        //1创建客户端通道

        try {
            SocketChannel client = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
            // 2 设置为非阻塞
            client.configureBlocking(false);
            // 3 创建缓冲区buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            Scanner input = new Scanner(System.in);
            while (true){
                System.out.println("请输入内容----------");
                String str = input.nextLine();
                System.out.println("你输入的是："+str);
                buffer.put((str + LocalDateTime.now().toString()).getBytes());
                buffer.flip();
                client.write(buffer);
                buffer.clear();
                if ("88".equals(str)){
                    break;
                }
            }
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void server(){
        // 1 创建服务端通道
        try {
            ServerSocketChannel server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(9898));
            server.configureBlocking(false);
            // 2创建选择器
            Selector selector = Selector.open();
            // 2.1将通道注册到选择器上,指定监听事件，类似于server.accepted();
            System.out.println("在监听连接选择器之前----");
            server.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("在监听连接选择器之后----");
            // 3 轮巡式获取选择器上的通道
            while (selector.select()>0){
                System.out.println("有事件准备就绪。。。。");
                // 至少有一个事件准备就绪
                // 4 获取所有事件的选择器
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                // 5 遍历选择器上的事件
                while (iterator.hasNext()){
                    // 6判断什么事件准备就绪
                    System.out.println("遍历事件。。。");
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isAcceptable()){
                        System.out.println("处理连接事件.......");
                        // 7 接受就绪
                        SocketChannel client = server.accept();
                        // 8 设置为非阻塞
                        client.configureBlocking(false);
                        // 9 注册读准备
                        client.register(selector,SelectionKey.OP_READ);
                    }else if (selectionKey.isReadable()){
                        System.out.println("处理读取事件....");
                        // 10 读事件准备就绪
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        // 11 读取数据
                        ByteBuffer buf = ByteBuffer.allocate(1024);
                        int len = 0;
                        while ((len = channel.read(buf))!= -1){
                            buf.flip();
                            System.out.println(new String(buf.array(),0,len));
                            buf.clear();
                        }
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
