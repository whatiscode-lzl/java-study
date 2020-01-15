package com.study.thread.t01;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Author liaozhenglong
 * @Date 2020/1/14 15:05
 *
 * 使用Latch代替wait 和notify
 *
 * 当不涉及同步并只是涉及线程通信的时候 使用synchronize 和wait 、notify显得太重
 * 这时可以考虑使用:countdownlatch/cyclicbarrier/semaphore
 **/
public class AwaitAndCountdown {
    List<Integer> lists = new ArrayList<>();

    public void  add(Integer integer){
        lists.add(integer);
    }

    public int size(){
        return lists.size();
    }

    public static void main(String[] args) {
        AwaitAndCountdown andCountdown = new AwaitAndCountdown();
        CountDownLatch latch = new CountDownLatch(1); // 设置为1，执行一次latch.countDown();门闩就开了

        new Thread(()->{
            System.out.println("启动监视集合容量的线程...");
            if (andCountdown.size()!=5){
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("监视集合容量结束");
        }).start();

        new Thread(()->{
            System.out.println("启动往集合添加数据的线程..");
            for (int i=0;i<10;i++){
                andCountdown.add(i);
                System.out.println("size=="+andCountdown.size());
                if (andCountdown.size()==5){
                    latch.countDown();
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("添加数据结束");
        }).start();
    }
}
