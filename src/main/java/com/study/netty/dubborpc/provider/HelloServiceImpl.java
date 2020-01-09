package com.study.netty.dubborpc.provider;

import com.study.netty.dubborpc.publicinterface.HelloServer;

/**
 * @Author liaozhenglong
 * @Date 2020/1/8 14:26
 **/
public class HelloServiceImpl implements HelloServer {

    /**
     * 服务端提供的api*/
    @Override
    public String hello(String msg) {
        return "你好客户端，我已经收到你的消息["+msg+"]";
    }

    @Override
    public String test(String msg) {
        return "测试方法";
    }
}
