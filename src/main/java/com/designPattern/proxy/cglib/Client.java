package com.designPattern.proxy.cglib;

/**
 * @Author liaozhenglong
 * @Date 2020/1/9 11:20
 **/
public class Client {

    public static void main(String[] args) {

        // 创建目标对象
        TeacherDao target = new TeacherDao();
        TeacherDao proxyIntance = (TeacherDao) new ProxyFactory(target).getProxyIntance();
        proxyIntance.hello();
    }
}
