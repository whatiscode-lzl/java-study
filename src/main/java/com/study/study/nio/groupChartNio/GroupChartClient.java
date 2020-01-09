package com.study.study.nio.groupChartNio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @Author liaozhenglong
 * @Date 2020/1/2 14:45
 **/
public class GroupChartClient {

    public static final String ADDRESS = "127.0.0.1";
    public static final int PORT = 9898;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    public GroupChartClient() throws IOException {
        socketChannel = SocketChannel.open(new InetSocketAddress(ADDRESS,PORT));
        socketChannel.configureBlocking(false);
        selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_READ);
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username+"->is ok");
    }

    // 处理发送消息
    private void sendMessages(String message){
        message = username+"say->"+message;
        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
        try {
            socketChannel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // d读取消息
    private void readMessages(){
        try {
            //System.out.println("等待消息.....");
            int select = selector.select(); // 该方法是阻塞的
            System.out.println("获取信息.....");
            if (select > 0){
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();

                    if (key.isReadable()){
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        SocketChannel channel = (SocketChannel)key.channel();
                        channel.read(buffer);
                        System.out.println(new String(buffer.array()));
                    }

                    iterator.remove();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        GroupChartClient groupChartClient = new GroupChartClient();
        new Thread(){
            public void run(){
                System.out.println("启动了一个处理读消息的线程..");
                while (true){
                    groupChartClient.readMessages();
                    try {
                        Thread.currentThread().sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String s = scanner.nextLine();
            groupChartClient.sendMessages(s);

        }

    }
}
