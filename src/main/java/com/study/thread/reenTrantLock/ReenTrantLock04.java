package com.study.thread.reenTrantLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author liaozhenglong
 * @Date 2020/1/14 16:46
 *
 * 调用lock的lockInterruptibly可以对Interrupt方法做出响应
 * 在一个线程等待的过程中可以被打断
 *
 **/
public class ReenTrantLock04 {

    Lock lock = new ReentrantLock();

    void m1(){
        System.out.println("m1..");
        lock.lock();
        try {
            System.out.println("m1.start");
            TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
            System.out.println("m1.end");
        }catch (InterruptedException e){
            System.out.println("m1被打断e="+e.getMessage());
        }finally {
            lock.unlock();
        }
    }

    void m2(){
        System.out.println("m2..");
        try {
            //lock.lock(); // 当申请不到锁的时候，即使t2.interrupt();也不会被打断
            lock.lockInterruptibly(); // 能被interrupt（）打断
            System.out.println("m2.start");
            TimeUnit.SECONDS.sleep(5);
            System.out.println("m2.end");
        }catch (InterruptedException e){
            System.out.println("m2被打断e");
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReenTrantLock04 lock04 = new ReenTrantLock04();

        Thread t1 = new Thread(lock04::m1);
        t1.start();

        Thread t2 = new Thread(lock04::m2);
        t2.start();

        TimeUnit.SECONDS.sleep(2);
        t2.interrupt();
    }
}
