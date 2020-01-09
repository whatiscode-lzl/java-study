package com.study.netty.dubborpc.customer;

import com.study.netty.dubborpc.netty.NettyClient;
import com.study.netty.dubborpc.publicinterface.HelloServer;

/**
 * @Author liaozhenglong
 * @Date 2020/1/8 16:08
 **/
public class ClientBootStrap {

    private static  String serviceName;

    public static void main(String[] args) {

        // 创建一个消费者
        NettyClient client = new NettyClient();

        HelloServer clientBean = (HelloServer) client.getBean(HelloServer.class, serviceName);
        String hello = clientBean.hello("你好啊~");
        System.out.println("----"+hello);
        System.out.println("------------------------------------");
        String test = clientBean.test("测试...");
        System.out.println("nio=="+test);
    }
}
