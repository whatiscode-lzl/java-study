package com.study.netty.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Author liaozhenglong
 * @Date 2020/1/7 17:54
 **/
public class TCPClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ClientTCPHandler());
        ChannelFuture future = bootstrap.connect("127.0.0.1", 9898).sync();
        future.channel().closeFuture();
    }
}
