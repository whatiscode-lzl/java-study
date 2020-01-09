package com.study.netty.singple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Author liaozhenglong
 * @Date 2020/1/3 11:17
 **/
public class NettyClient {

    public static void main(String[] args) throws InterruptedException {

        // 创建线程组
        EventLoopGroup group = new NioEventLoopGroup();

        // 创建启动器并配置参数
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyClientHandler());
                        }
                    });
            System.out.println("客户端准备就绪...");

            // 连接到服务器
            ChannelFuture cf = bootstrap.connect("127.0.0.1", 9898).sync();
            // 关闭
            cf.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }
    }
}
