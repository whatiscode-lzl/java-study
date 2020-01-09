package com.study.study.nio.filechannel.buffer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @Author liaozhenglong
 * @Date 2019/12/31 16:27
 **/
public class ScateAndGatterTest {

    public static void main(String[] args) {
        // 创建服务端通道
        try {
            ServerSocketChannel server = ServerSocketChannel.open();
            server.socket().bind(new InetSocketAddress(9898));

            // 创建缓冲数组
            ByteBuffer[] buffers = new ByteBuffer[2];
            buffers[0] = ByteBuffer.allocate(5);
            buffers[1] = ByteBuffer.allocate(3);
            System.out.println("等待客户链接。。。buffer"+buffers.length);
            SocketChannel client = server.accept();
            System.out.println("有客户链接。。。client =="+client);
            int read = 0;
            int messageLength = 8;
            while (true){
                while (read <  messageLength){
                    int l = (int)client.read(buffers);
                    read +=l;
                    System.out.println("read:"+read);
                    // 使用流打印，查看当前的position和limit
                    Arrays.asList(buffers).stream().map(buffer ->"position="+buffer.position()+",limit="+buffer.limit())
                            .forEach(System.out::println);

                }
                // 将所有的buffer flip()
                Arrays.asList(buffers).stream().forEach(buffer ->buffer.flip());
                System.out.println("-0--------------------");
                Arrays.asList(buffers).stream().map(buffer ->"position="+buffer.position()+",limit="+buffer.limit())
                        .forEach(System.out::println);
                // 将数据写出
                long write = 0;
                while (write < messageLength){
                    //System.out.println("write:"+write);
                    long write1 = client.write(buffers);
                    //System.out.println("writel:"+write1);
                    write +=write1;
                }

                System.out.println("read:"+read+",messageLength:"+messageLength+",write:"+write);
                Arrays.asList(buffers).forEach(buffer -> buffer.clear());
                read=0;
                write=0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
