package com.study.thread.reenTrantLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author liaozhenglong
 * @Date 2020/1/14 16:10
 * reenrantlock 用于替代synchronize
 *  * 本例中中由于m1锁定this，需要等到m1执行完毕后，m2才能得到执行
 *  * 这里是复习synchronize的
 *
 *  但需要注意syn在发生异常的时候会自动释放锁，而reentrantlock不会，所以需要在finally中手动释放锁
 **/
public class ReenTrantLock02 {

    Lock  lock = new ReentrantLock();

    void m1(){
        System.out.println("m1...");
        try {
            lock.lock();
            for (int i = 0;i<10;i++){
                System.out.println("i=="+i);
                TimeUnit.SECONDS.sleep(1);
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }

    void  m2(){
        System.out.println("m2..");
        try {
            lock.lock();
            System.out.println("执行m2下的lock");
        }catch (Exception e){

        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReenTrantLock02 lock02 = new ReenTrantLock02();
        new Thread(()->{
            lock02.m1();
        }).start();
        TimeUnit.SECONDS.sleep(1);
        new Thread(()->{
            lock02.m2();
        }).start();
    }
}
