package com.study.thread.container;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author liaozhenglong
 * @Date 2020/1/15 10:21
 *
 * 使用Lock和Condition来实现生产和消费
 * Condition的方式可以更加精确指定哪些线程被唤醒
 **/
public class MyContainer02<T> {
    private final LinkedList<T> lists = new LinkedList<>();
    private final int MAX = 10;
    private int count = 0;

    Lock lock = new ReentrantLock();
    Condition producer = lock.newCondition();
    Condition consumer = lock.newCondition();
    public void put(T t){
        lock.lock();
        try {
            while (count == MAX){
                producer.await();
            }
            count++;
            lists.add(t);
            consumer.signalAll();// 通知消费线程进行消费
        }catch (Exception e){

        }finally {
            lock.unlock();
        }
    }

    private T get(){
        lock.lock();
        T t = null;
        try {
            while (count == 0){
                consumer.await();
            }
            t = lists.removeFirst();
            count--;
            producer.signalAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return  t;
    }

    public static void main(String[] args) {
        MyContainer02<String> mc = new MyContainer02<>();

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
