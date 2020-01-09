package com.study.nio.ch;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ServerSocketChannelTest {
//客户端数据发送一个读写请求到服务端
//    服务无端不能确定客户端判断数据真实有效状态时候，该线程会一直处于阻塞状态，服务端会等客户端发送
//
//    服务端会判断数据在内核地址空间中是否存在，此时服务端线程阻塞，原来是多线程来解决，为每一个请求分配一个线程来处理请求，充分利用cpu资源，
//    后续请求，线程没有100%的利用，
//
//    NIIO--非阻塞式： 通道加缓冲区，
//    把每一个用于传输数据的通道注册到选择器，选择器实时监控通道上的状况，
//    当某一个通道上的某一个请求的事件完全准备就绪时，选择器才会将这个任务分配到服务端的一个或多个线程上在去运行
//
//
//
//
//    读数据状态--完全准备就绪
//    选择器：Selector
//
//===============================
//    一、使用NIO完成网络通信的三个核心
//1、通道（Channel:：负责连接
//    java.nio.channels.Channel
//                                  |--SelectableChannel
//                                  |--ServerSocketChannel
//                                  |--DatagramChannel
//
//                                  |--Pipe.SinkChannel
//                                  |--Pipe.SourceChannel
//
//    FileChannel不能切换成非阻塞模式
//
//
//2、缓冲区（Buffer）：负责数据的存取
//
//3、选择器（Selector）：是SelectableChannel的多路复涌去。用于监控SelectableChannel的IO情况
//
//

    public static void main(String[] args) throws InterruptedException {
        long s1 = System.currentTimeMillis();
        Runnable server = new Runnable() {
            @Override
            public void run() {
                new ServerSocketChannelTest().server();
            }
        };

        Runnable client = new Runnable() {
            @Override
            public void run() {
                new ServerSocketChannelTest().client();

            }
        };


        //todo 为何两个线程启动 不是串行的

        server.run();
        Thread.sleep(100L);
        client.run();

        System.out.println("END=" + (System.currentTimeMillis() - s1));
    }


    //客户端
    @Test
    public void client() {
        //1、获取通道
        SocketChannel socketChannel = null;
        FileChannel inChannel = null;
        try {
            socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

            //测试中是发送一个图片给server，这里用FileChannel去获取
            inChannel = FileChannel.open(Paths.get("T:/data/nio/1.jpg"), StandardOpenOption.READ);

            //2、分配指定的大小的缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            //3、发送数据

            //long transferTo = inChannel.transferTo(0, inChannel.size(), socketChannel);

            while (inChannel.read(buffer) != -1) {
                buffer.flip();
                socketChannel.write(buffer);
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("结果={}", e);
        } finally {
            if (inChannel != null) {
                try {
                    inChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("结果={}", e);
                }
            }

            if (socketChannel != null) {
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("结果={}", e);
                }
            }
        }
    }

    //服务器--阻塞式
    @Test
    public void server() {
        ServerSocketChannel ssChannel = null;
        SocketChannel acceptSocketChannel = null;
        FileChannel outChannel = null;
        try {
            //1、获取通道
            ssChannel = ServerSocketChannel.open();

            outChannel = FileChannel.open(Paths.get("T:/data/nio/_21.jpg"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);


            //2、绑定连接
            ssChannel.bind(new InetSocketAddress(9898));

            //3、获取客户端连接的通道
            acceptSocketChannel = ssChannel.accept();


            //4、分配指定大小的缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);


            //5、接收客户端的数据，并保存到本地
            while (acceptSocketChannel.read(buffer) != -1) {
                buffer.flip();
                outChannel.write(buffer);
                buffer.clear();
            }

        } catch (IOException e) {
            e.printStackTrace();
            log.error("结果={}", e);
        } finally {

            if (ssChannel != null) {
                try {
                    ssChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("结果={}", e);
                }
            }
            //acceptSocketChannel
            if (acceptSocketChannel != null) {
                try {
                    acceptSocketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //outChannel
            if (outChannel != null) {
                try {
                    outChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("结果={}", e);
                }
            }
        }
    }


}
