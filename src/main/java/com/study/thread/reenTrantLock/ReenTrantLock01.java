package com.study.thread.reenTrantLock;

import java.util.concurrent.TimeUnit;

/**
 * @Author liaozhenglong
 * @Date 2020/1/14 15:58
 *
 * reenrantlock 用于替代synchronize
 * 本例中中由于m1锁定this，需要等到m1执行完毕后，m2才能得到执行
 * 这里是复习synchronize的
 **/
public class ReenTrantLock01 {

    synchronized void m1(){
        for (int i=0;i<10;i++){
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("i="+i);
        }
    }

    synchronized void m2(){
        System.out.println("m2...");
    }

    public static void main(String[] args) throws InterruptedException {
        ReenTrantLock01 lock01 = new ReenTrantLock01();
        new Thread(()->{
            lock01.m1();
        }).start();
        TimeUnit.SECONDS.sleep(1);
        lock01.m2();
    }
}
