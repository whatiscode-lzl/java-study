package com.study.study.nio.filechannel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author liaozhenglong
 * @Date 2019/12/31 12:23
 **/
public class NioChannel {

    public static void main(String[] args) {
        try {
            String str = "hello 2020年";
            // 创建文件流
            FileOutputStream fos = new FileOutputStream("e://f01.txt");
            // 创建通道
            FileChannel fosChannel = fos.getChannel();
            // 创建缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            // 把数据放入到缓冲区
            buffer.put(str.getBytes());
            // 把数据写入到目标通道（目标流）
            buffer.flip();
            fosChannel.write(buffer);
            // 关闭流
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
