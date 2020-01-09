package com.study.nio.io;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

/**
 * @Author liaozhenglong
 * @Date 2019/12/30 15:47
 **/
@Slf4j
public class NoBlockingNio2Test {


    //发送端
    @Test
    public void send() {
        DatagramChannel datagramChannel = null;
        try {
             datagramChannel = DatagramChannel.open();
            datagramChannel.configureBlocking(false);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put("测试1234".getBytes());
            buffer.flip();
            datagramChannel.send(buffer, new InetSocketAddress("127.0.0.1", 8080));
            buffer.clear();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (datagramChannel != null) {
                try {
                    datagramChannel.close();
                    System.out.println("发送完成关闭 DatagramChannel！");
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("e={}", e);
                }

            }
        }
    }


    //接受端
    @Test
    public void serverDatagramChannel() {
        DatagramChannel serverDatagramChannel = null;
        try {
            serverDatagramChannel = DatagramChannel.open();
            serverDatagramChannel.configureBlocking(false);
            serverDatagramChannel.bind(new InetSocketAddress(8080));

            Selector selector = Selector.open();
            serverDatagramChannel.register(selector, SelectionKey.OP_READ);

            while (selector.select() > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey sk = iterator.next();
                    if (sk.isReadable()) {
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        serverDatagramChannel.receive(buffer);
                        buffer.flip();
                        System.out.println(new String(buffer.array(), 0, buffer.limit()));
                        buffer.clear();
                    }
                }
                iterator.remove();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (serverDatagramChannel != null) {
                try {
                    serverDatagramChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("e={}", e);
                }

            }
        }


    }

}
