package com.study.study.nio.filechannel.buffer;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author liaozhenglong
 * @Date 2019/12/31 15:45
 *
 * MappedByteBuffer 把文件数据映射到内存中，操作系统不需要把文件copy,对其进行修改不会影响到原来的文件
 **/
public class MappedByteBufferTest {

    public static void main(String[] args) {
        // 创建随机读取文件流
        try {
            RandomAccessFile raf = new RandomAccessFile("e://f01.txt", "rw");
            FileChannel rafChannel = raf.getChannel();
            // 获取MappedByteBuffer
            MappedByteBuffer map = rafChannel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
            map.put(0,(byte)'H');
            map.put(3,(byte)'G');
            map.put(5,(byte)'A'); // IndexOutOfBoundsException 因为size是5，不是下表为5
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
