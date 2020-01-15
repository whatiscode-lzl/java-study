package com.study.thread.threadLocal;

import java.util.concurrent.TimeUnit;

/**
 * @Author liaozhenglong
 * @Date 2020/1/15 10:41
 **/
public class ThreadLocal01 {

    volatile static Person p = new Person();

    public static void main(String[] args) {
        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(p.name);
        }).start();

        new Thread(()->{
            p.name = "李四";
        }).start();
    }
    static class Person{
        String name = "向三";
    }
}
