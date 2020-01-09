package com.study.study.nio.filechannel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author liaozhenglong
 * @Date 2019/12/31 14:27
 **/
public class NioChannel03 {
    public static void main(String[] args) {
        // 创建源文件通道
        File file = new File("e://f01.txt");
        try {
            FileInputStream fis = new FileInputStream(file);
            FileChannel source = fis.getChannel();
            // 创建目标文件通道
            FileOutputStream fos = new FileOutputStream("e://f02.txt");
            FileChannel destChannel = fos.getChannel();

            // 创建缓冲流
            ByteBuffer buffer = ByteBuffer.allocate(2);
            while(true){
                buffer.clear();
                int read = source.read(buffer);
                System.out.println("read:"+read);
                if(read == -1){
                    break;
                }
                buffer.flip();
                destChannel.write(buffer);
            }
            fis.close();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
