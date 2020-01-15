package com.study.thread.container;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * @Author liaozhenglong
 * @Date 2020/1/14 17:34
 *
 * 编一个固定容量同步容器，用户put get 和getCount方法
 * 能够支持两个生产者线程的调用和10个消费者的调用
 * 使用wait notify 和notifyAll
 *
 **/
public class MyContainer01<T> {

    final private LinkedList<T> lists = new LinkedList<>();
    final private int MAX = 10;
    private int count = 0;

    public synchronized void put(T t){
        while (count == MAX){
            System.out.println("put当前线程:"+Thread.currentThread().getName());
            try {
                this.wait();
                System.out.println("put当前被唤醒的线程:"+Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
            lists.add(t);
            ++count;
            notifyAll();
    }

    public synchronized T get(){
        while (count == 0){
            System.out.println("get当前线程:"+Thread.currentThread().getName());
            try {
                this.wait();
                System.out.println("get当前被唤醒线程:"+Thread.currentThread().getName());
            } catch (InterruptedException e) {
                return null;
            }
        }
        --count;
        notifyAll();
        return lists.removeFirst();
    }

    public static void main(String[] args) {
        MyContainer01<String> mc = new MyContainer01<>();

        // 启动消费者线程
        for (int i=0;i<10;i++){
            new Thread(()->{
                for (int j=0;j<5;j++)System.out.println(mc.get());
            },"mc"+i).start();
        }

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 启动生产者线程
        for (int i=0;i<2;i++){
            new Thread(()->{
                for (int j=0;j<25;j++)mc.put(Thread.currentThread().getName()+ "-- "+j);
            },"pm"+i).start();
        }
    }
}
