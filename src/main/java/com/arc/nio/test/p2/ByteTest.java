package com.arc.nio.test.p2;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Z
 */
@SpringBootApplication
public class ByteTest {
//缓冲区 Buffer， Java NIO中负责数据的存取。用于存取不同数据类型
//        ByteBuffer
//        CharBuffer
//        ShortBuffer
//        IntBuffer
//        LongBuffer
//        FloatBuffer
//        DoubleBuffer
//    上述缓存区的管理方式几乎一致，通过allocate()获取缓冲区

//    二、缓冲区存取数据的两个核心方法
//    put()：存入缓冲区数据
//    get()：获取缓冲区中的数据
//
//    三、缓冲区的4个核心指标属性
//    capacity：容量，表示缓冲区中最大存储数据的容量，一旦声明不能改变
//     limit：界限，表示缓冲区中路操作数据的大小，（limit后数据不能读写）
//    position：位置，表示缓存区正在操作数据的位置
//
//
//    position <=limit <=capacity
//
//

    public static void main(String[] args) {


    }

}
