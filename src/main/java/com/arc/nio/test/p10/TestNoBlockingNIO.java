package com.arc.nio.test.p10;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author {author}
 * @since 2019/12/9 12:59
 */
@Slf4j
public class TestNoBlockingNIO {
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
//


    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("请输入一个字符串(中间能加空格或符号)");
        String line = input.nextLine();
        String s = LocalDateTime.now().toString() + "\n" + line;
        System.out.println(s);
    }


    //客户端
    @Test
    public void client() {
        SocketChannel socketChannel = null;
        FileChannel inChannel = null;
        try {

            //1、获取通道
            socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

            //2、切换成非阻塞模式
            socketChannel.configureBlocking(false);

            //3、分配缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            //测试中是发送一个 时间字符串给server
            //
            String msg = getMsg();
            buffer.put(msg.getBytes());
            buffer.flip();

            //4、发送数据
            socketChannel.write(buffer);
            buffer.clear();


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

    private String getMsg() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("请输入：");
//        while (scanner.hasNext()) {
//            String msg = scanner.next();
//        }
//        return "null";

        Scanner input = new Scanner(System.in);
        System.out.println("请输入一个字符串(中间能加空格或符号)");
        String line = input.nextLine();
        String s = LocalDateTime.now().toString() + "\n" + line;
        System.out.println(s);
        return s;

    }

//        System.out.println("请输入一个字符串(中间不能加空格或符号)");
//        String b = input.next();
//        System.out.println("请输入一个整数");
//        int c;
//        c = input.nextInt();
//        System.out.println("请输入一个double类型的小数");
//        double d = input.nextDouble();
//        System.out.println("请输入一个float类型的小数");
//        float f = input.nextFloat();
//        System.out.println("按顺序输出abcdf的值：");
//        System.out.println(a);
//        System.out.println(b);
//        System.out.println(c);
//        System.out.println(d);
//        System.out.println(f);


    //服务器--阻塞式
    @Test
    public void server() {
        ServerSocketChannel ssChannel = null;
        SocketChannel acceptSocketChannel = null;
        FileChannel outChannel = null;
        try {
            //1、获取通道
            ssChannel = ServerSocketChannel.open();
            //2、设置非阻塞
            ssChannel.configureBlocking(false);
            //3、绑定连接
            ssChannel.bind(new InetSocketAddress(9898));
            //4、获取选择器
            Selector selector = Selector.open();

            //5、将通道注册到选择器,并且指定“监听事件”  ，对比之前的 accept方法=阻塞
            ssChannel.register(selector, SelectionKey.OP_ACCEPT);

            //6、轮询式的获取选择器上“已经准备就绪”的事件
            while (selector.select() > 0) {  //至少有一个准备就绪的事件

                //7、包括所有注册的“选择键（已就绪的监听事件）”
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                //8、获取准备就绪的事件
                while (iterator.hasNext()) {
                    //9、判断具体是什么准备就绪事件
                    SelectionKey sk = iterator.next();
                    if (sk.isAcceptable()) {
                        //10、若"接受就绪"，获取客户端连接
                        SocketChannel sChannel = ssChannel.accept();
                        //11、设置非阻塞模式
                        sChannel.configureBlocking(false);
                        //12、将该通道注册到选择器上
                        sChannel.register(selector, SelectionKey.OP_READ);
                    } else if (sk.isReadable()) {
                        //13、获取当前选择器上“读就绪”状态的通道
                        SocketChannel sChannel = (SocketChannel) sk.channel();
                        //14、读取数据
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int len = 0;
                        while ((len = sChannel.read(buffer)) > 0) {
                            buffer.flip();
                            System.out.println(new String(buffer.array(), 0, len));
                            buffer.clear();
                        }
                    }
                    //15、取消选择键
                    iterator.remove();
                }

            }

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
