package com.study.netty.handlerChain;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Author liaozhenglong
 * @Date 2020/1/7 16:06
 **/
public class TestHandlerChainClient {

    public static void main(String[] args) throws InterruptedException {

        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ClientInitializer());
        ChannelFuture future = bootstrap.connect("localhost", 9898).sync();
        future.channel().closeFuture().sync();
    }
}
