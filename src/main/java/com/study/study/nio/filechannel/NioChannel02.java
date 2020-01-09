package com.study.study.nio.filechannel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @Author liaozhenglong
 * @Date 2019/12/31 14:01
 **/
public class NioChannel02 {
    public static void main(String[] args) {
        // 创建文件输入通道
        try {
            FileChannel fileChannel = FileChannel.open(Paths.get("e://f01.txt"));
            System.out.println("通道大小=="+fileChannel.size());
            // 创建缓冲区buffer
            ByteBuffer buffer = ByteBuffer.allocate((int)fileChannel.size());
            // 把数据读到缓冲区
            fileChannel.read(buffer);
            System.out.println("f01文件的内容=="+new String(buffer.array()));
            fileChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
