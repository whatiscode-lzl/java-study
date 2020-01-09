package com.study.study.nio.filechannel;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @Author liaozhenglong
 * @Date 2019/12/31 14:42
 **/
public class NioChannel04 {
    public static void main(String[] args) {
        // 创建源文件通道
        try {
            long start = System.currentTimeMillis();
            FileChannel src = FileChannel.open(Paths.get("D:\\chromDownload\\CentOS-7-x86_64-DVD-1908.iso"));
            // 创建目标通道
            FileChannel dest = FileChannel.open(Paths.get("f://centos.iso"), StandardOpenOption.READ,StandardOpenOption.WRITE,StandardOpenOption.CREATE);

            long log = dest.transferFrom(src,0,src.size());
            long end = System.currentTimeMillis();
            System.out.println(log+" 用时=="+(end -start));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
