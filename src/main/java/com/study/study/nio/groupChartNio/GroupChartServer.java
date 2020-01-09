package com.study.study.nio.groupChartNio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @Author liaozhenglong
 * @Date 2020/1/2 13:41
 **/
public class GroupChartServer {

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    public static final int PORT = 9898;

    // 初始化服务端
    public GroupChartServer(){
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));

            // 注册到选择器
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 处理连接
    public void handleConnetion(){
        while (true){
            try {
                int select = selector.select();
                if (select == 0){
                    System.out.println("等待事件触发.......");
                }
                // 获取事件
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();

                    // 处理连接事件
                    if (key.isAcceptable()){
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector,SelectionKey.OP_READ);
                        System.out.println("新用户:"+socketChannel);
                    }

                    // 处理读事件
                    if (key.isReadable()){
                        System.out.println("客户发了消息...");
                        readMessages(key);
                    }
                    iterator.remove();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void readMessages(SelectionKey key){
        SocketChannel channel = null;
        try {
            channel= (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);
            if (count > 0){
                buffer.flip();
                String str = new String(buffer.array());
                System.out.println("from client:"+str);
                // 转发消息
                sendMessagesToOthers(str,channel);
            }
        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress()+"离线了");
                key.cancel();
                channel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private void sendMessagesToOthers(String str, SocketChannel channel) throws IOException {

        for (SelectionKey key : selector.keys()){
            if (key.channel() instanceof SocketChannel && key.channel()!=channel){
                ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
                ((SocketChannel) key.channel()).write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        GroupChartServer server = new GroupChartServer();
        server.handleConnetion();
    }

    public void run() {
    }
}
