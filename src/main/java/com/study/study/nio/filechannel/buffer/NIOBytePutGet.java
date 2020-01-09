package com.study.study.nio.filechannel.buffer;

import java.nio.ByteBuffer;

/**
 * @Author liaozhenglong
 * @Date 2019/12/31 15:09
 **/
public class NIOBytePutGet {
    public static void main(String[] args) {
        // 把不同的类型的buffer放入put缓冲区时要按顺序get
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.putInt(10086);
        buffer.putShort((short)40);
        buffer.putChar('中');
        buffer.putLong(10);

        buffer.flip();

        System.out.println(buffer.getInt());
        System.out.println(buffer.getShort());
        //System.out.println(buffer.getChar());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getLong());
    }
}
