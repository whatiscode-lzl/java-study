package com.study.thread.reenTrantLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author liaozhenglong
 * @Date 2020/1/14 16:23
 *
 * 使用reentrantLock可以进行尝试锁定 trylock,这样无法锁定或者在指定时间内无法锁定线程可以决定是否继续等待
 **/
public class ReenTrantLock03 {

    Lock lock = new ReentrantLock();

    void m1(){
        System.out.println("m1..");
        lock.lock();
        try {
            for (int i = 0; i<10; i++){
                System.out.println("i="+i);
                TimeUnit.SECONDS.sleep(1);
            }
        }catch (Exception e){
            System.out.println("m1="+e.getMessage());
        }finally {
            lock.unlock();
        }
    }

    void m2() {
        System.out.println("m2...");
        /**
         * 使用tryLock进行锁定，不管锁定与否，方法都将继续执行
         * 可以根据boolean tryLock();的返回值判定是否有锁定
         *
         * boolean tryLock(long time, TimeUnit unit) 在指定时间后继续执行*/
        boolean isLock = lock.tryLock();
        System.out.println("执行m2下的isLock="+isLock);
            try {
                for (int i = 0;i < 5;i++){
                    System.out.println("y=="+i);
                }
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
               System.out.println("m2="+e.getMessage());
            }finally {
                lock.unlock();
            }


    }

    public static void main(String[] args) throws InterruptedException {
        ReenTrantLock03 lock03 = new ReenTrantLock03();

        new Thread(()->{
            lock03.m1();
        }).start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(()->{
            lock03.m2();
        }).start();
    }
}
