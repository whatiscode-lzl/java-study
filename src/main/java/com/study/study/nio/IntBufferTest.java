package com.study.study.nio;


import java.nio.IntBuffer;

/**
 * @Author liaozhenglong
 * @Date 2019/12/31 10:27
 **/
public class IntBufferTest {
    // 测试Buffer
    public static void main(String[] args) {
        test();
    }

    public static void test(){
        IntBuffer intBuffer = IntBuffer.allocate(5);
        System.out.println("capacity = "+intBuffer.capacity());
        System.out.println("position = "+intBuffer.position());
        System.out.println("limit = "+intBuffer.limit());
        System.out.println("mark = "+intBuffer.mark());
        System.out.println("------------------------------");
        for (int i=0;i<intBuffer.capacity()-1;i++){
            intBuffer.put(i*3);
        }
        System.out.println("capacity = "+intBuffer.capacity());
        System.out.println("position = "+intBuffer.position());
        System.out.println("limit = "+intBuffer.limit());
        System.out.println("mark = "+intBuffer.mark());
        System.out.println("==============================");
        intBuffer.flip();
        System.out.println("capacity = "+intBuffer.capacity());
        System.out.println("position = "+intBuffer.position());
        System.out.println("limit = "+intBuffer.limit());
        System.out.println("mark = "+intBuffer.mark());
        System.out.println("------------------------------");
        for (int i = 0; i < intBuffer.limit();i++){
            System.out.println(intBuffer.get());
        }
        intBuffer.clear();
        System.out.println("------------clear...------------------");
        System.out.println("capacity = "+intBuffer.capacity());
        System.out.println("position = "+intBuffer.position());
        System.out.println("limit = "+intBuffer.limit());
        System.out.println("mark = "+intBuffer.mark());

        for (int i = 0; i < intBuffer.limit();i++){
            System.out.println(intBuffer.get());
        }
        System.out.println("------------++++++------------------");
        while (intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }
    }
}
