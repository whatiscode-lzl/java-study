package com.study.study.nio.filechannel.buffer;

import java.nio.ByteBuffer;

/**
 * @Author liaozhenglong
 * @Date 2019/12/31 15:27
 **/
public class ReadOnlyBufferTest {
    public static void main(String[] args) {

        ByteBuffer buffer = ByteBuffer.allocate(64);
        for (int i = 0;i<buffer.capacity(); i++){
            buffer.put((byte)i);
        }
        // 只读类型的buffer
        ByteBuffer onlyBuffer = buffer.asReadOnlyBuffer();
        onlyBuffer.flip();
        while (onlyBuffer.hasRemaining()){
            System.out.println(onlyBuffer.get());
        }
        onlyBuffer.flip();
        onlyBuffer.put((byte)'中');// ReadOnlyBufferException
    }
}
