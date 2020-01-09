package com.study.netty.dubborpc.provider;

import com.study.netty.dubborpc.netty.NettyServer;

/**
 * @Author liaozhenglong
 * @Date 2020/1/8 15:04
 **/
public class RPCServer {

    public static void main(String[] args) {
        NettyServer.startServer("127.0.0.1",9898);
    }
}
