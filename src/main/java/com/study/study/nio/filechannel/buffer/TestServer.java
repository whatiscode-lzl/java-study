package com.study.study.nio.filechannel.buffer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @Author liaozhenglong
 * @Date 2019/12/31 17:01
 **/
public class TestServer {
    public static void main(String[] args) {
        try {
            ServerSocketChannel server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(9898));
            System.out.println("等待客户链接....");
            SocketChannel client = server.accept();
            System.out.println("client:"+client);

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int len =0;
            while ((len = client.read(buffer)) !=-1){
                buffer.flip();
                System.out.println("客户端说："+new String(buffer.array(),0,len));
                buffer.clear();
            }
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
