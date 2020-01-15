package com.study.thread.threadLocal;

import java.util.concurrent.TimeUnit;

/**
 * @Author liaozhenglong
 * @Date 2020/1/15 10:48
 *
 * 当有多个线程并发操作一个数据时，即使没有给这个数据声明为volatile,其中的一个线程改变这个数据，其它的线程也是有可能
 * 访问到改变了的数据，要避免这种情况可以使用ThreadLocal线程局部变量
 *
 * ThreadLocal是使用空间换时间，而synchronized是使用时间换空间
 * 比如hibernate的session就存在于ThreadLocal中，避免synchronized的使用
 **/
public class ThreadLocal02 {

    //volatile static Person p = new Person();

    static ThreadLocal<Person> lp = new ThreadLocal<>();

    public static void main(String[] args) {
        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(lp.get());
        }).start();

        new Thread(()->{
            lp.set(new Person());
        }).start();
    }
    static class Person{
        String name = "zhangsan";
    }
}
