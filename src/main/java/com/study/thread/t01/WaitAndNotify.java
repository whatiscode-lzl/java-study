package com.study.thread.t01;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author liaozhenglong
 * @Date 2020/1/14 14:38
 *
 * 使用wait()和notify()方法实现如下需求：
 * 启动两个线程，其中一个线程忘集合里添加数据，
 * 另一个线程监听到该集合的数据达到5的时候就给出提示
 *
 **/
public class WaitAndNotify {

    List<Integer> list = new ArrayList<>();

    public  void add(Integer integer){
        this.list.add(integer);
    }

    public  int size(){
        return list.size();
    }
    public static void main(String[] args) {
        WaitAndNotify t = new WaitAndNotify();

        new Thread(()->{
            synchronized (t){
                System.out.println("启动监视集合数据的线程");
                if (t.size()!=5) {
                    try {
                        t.wait(); // 会释放锁
                        System.out.println("集合的size=="+t.size());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    t.notify(); // 不会释放锁
                }
                System.out.println("监视集合数据结束");
            }
        }).start();
        new Thread(()->{
            synchronized (t){
                System.out.println("启动往集合里添加数据的线程..");
                for (int i = 1;i<10;i++){
                    t.add(i);
                    System.out.println("当前集合大小=="+t.size());
                    if (t.size()==5){
                        t.notify(); // 应为不会释放锁，所以需要主动释放
                        try {
                            t.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println("添加数据工作结束");
            }
        }).start();
    }
}
