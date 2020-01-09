package com.study.netty.dubborpc.publicinterface;

/**
 * @Author liaozhenglong
 * @Date 2020/1/8 14:24
 **/
/**
 * 服务端和消费端公共的方法*/
public interface HelloServer {
    String hello(String msg);

    String test(String msg);
}
