package com.study.thread.reenTrantLock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author liaozhenglong
 * @Date 2020/1/14 17:11
 *
 * ReentrantLock 还可以指定为公平锁 ,哪个线程等待的时间长，谁就拥有那把锁 (但效率比较低)
 **/
public class ReenTrantLock05 extends Thread {
    static Lock lock = new ReentrantLock(true);// true表示为公平锁

    public void run(){
        for (int i = 0;i<100;i++){
            lock.lock();
            System.out.println(Thread.currentThread().getName()+"获得锁");
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ReenTrantLock05 lock05 = new ReenTrantLock05();

        new Thread(lock05).start();
        new Thread(lock05).start();
        new Thread(lock05).start();
    }
}
