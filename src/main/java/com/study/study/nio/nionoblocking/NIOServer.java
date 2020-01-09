package com.study.study.nio.nionoblocking;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author liaozhenglong
 * @Date 2020/1/2 10:25
 **/
public class NIOServer {
    public static void main(String[] args) throws Exception {

        // 1 创建服务端通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 2 绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress(9898));

        // 3 设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 4 创建选择器
        Selector selector = Selector.open();

        // 5 把通道注册到选择器，并绑定事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环遍历选择器中的SelectionKey(不同的SelectionKey对应不同的客户端通道)

        while (true){
            // 6 阻塞一秒后轮训selector是否有SelectionKey
            if (selector.select(1000)==0){
                // 7 没有事件SelectionKey立即进入阻塞
                System.out.println("没有轮训到事件....");
                continue;
            }
            // 8 轮训到SelectionKey
            Set<SelectionKey> selectionKeySet = selector.selectedKeys();

            // 9 使用迭代器遍历该集合（应为涉及到要在遍历过程中删除出元素）
            Iterator<SelectionKey> selectionKeyIterator = selectionKeySet.iterator();
            System.out.println("遍历之前selectionKeySet.size()=="+selectionKeySet.size());
            while (selectionKeyIterator.hasNext()){
                SelectionKey selectionKey = selectionKeyIterator.next();

                // 10 selectionKey的事件是否为SelectionKey.OP_ACCEPT
                if (selectionKey.isAcceptable()){
                    System.out.println("客户端请求连接.....");
                    SocketChannel socketChannel = serverSocketChannel.accept();

                    // 11 注册到选择器
                    socketChannel.configureBlocking(false); // 不设置的话回报 IllegalBlockingModeException的异常
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024)); // IllegalBlockingModeException
                    System.out.println("连接到客户--》"+socketChannel);
                }

                // 12 selectionKey的事件是否为SelectionKey.OP_READ
                if (selectionKey.isReadable()){
                    System.out.println("客户端发来了消息");
                    // 13 获取该key对应的通道
                    SocketChannel socketChannel = (SocketChannel)selectionKey.channel();

                    // 14 获取缓冲流
                    ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();

                    // 15 读取数据
                    /*while (socketChannel.read(byteBuffer) !=-1){
                        byteBuffer.flip();
                        System.out.println("客户端说:"+new String(byteBuffer.array()));
                        byteBuffer.clear();
                    }*/
                    socketChannel.read(byteBuffer);
                    byteBuffer.flip();
                    System.out.println("客户端说:"+new String(byteBuffer.array()));
                    byteBuffer.clear();

                }
                // 16 删除处理完的事件
                System.out.println("删除前selectionKeySet.size()=="+selectionKeySet.size());
                selectionKeyIterator.remove();
                System.out.println("删除后selectionKeySet.size()=="+selectionKeySet.size());
            }
            System.out.println("遍历完后selectionKeySet.size()=="+selectionKeySet.size());
        }
    }
}
