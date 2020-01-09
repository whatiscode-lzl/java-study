package com.designPattern.proxy.cglib;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author liaozhenglong
 * @Date 2020/1/9 11:01
 **/
public class ProxyFactory extends Client implements MethodInterceptor {

    // 维护一个目标对象
    Object target;
    public ProxyFactory(Object target){
        this.target = target;
    }

    // 返回一个代理对象:就是target的代理对象
    public Object getProxyIntance(){
        // 创建一个工具类
        Enhancer enhancer = new Enhancer();
        // 设置父类
        enhancer.setSuperclass(target.getClass());
        // 设置回调函数
        enhancer.setCallback(this);
        // 创建子类对象，及代理对象
        return enhancer.create();
    }

    // 会调用目标对象的相关的方法
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("cglib的代理模式开始。。。");
        Object resVal = method.invoke(target, objects);
        System.out.println("cglib的代理模式提交。。。");
        return resVal;
    }
}
