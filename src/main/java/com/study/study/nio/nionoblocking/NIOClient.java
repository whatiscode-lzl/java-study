package com.study.study.nio.nionoblocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Author liaozhenglong
 * @Date 2020/1/2 11:29
 **/
public class NIOClient {

    public static void main(String[] args) throws IOException {
        // 1 创建客户端通道
        SocketChannel socketChannel = SocketChannel.open();

        // 2 设置为非阻塞模式
        socketChannel.configureBlocking(false);

        // 3 设置服务器地址
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 9898);

        if (!socketChannel.connect(address)){
            // 没有连接上服务器
            while (!socketChannel.finishConnect()){
                System.out.println("正在连接.....");
            }
        }

        // 4 发送消息
        String str = "hello,世界";

        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());

        socketChannel.write(buffer);
        while (true){

        }
    }
}
