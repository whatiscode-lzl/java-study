package com.study.netty.codec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;

/**
 * @Author liaozhenglong
 * @Date 2020/1/3 10:26
 **/
public class NettyServer {

    public static void main(String[] args) throws Exception {

        // 创建BossGroup线程组 和WorkGroup线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        // 创建服务端的启动对象，并配置启动参数
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,128)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        // 添加protocolBuf的解码器， 这里需要制定要被解码的对象
                        pipeline.addLast(new ProtobufDecoder(StudentPOJO.Student.getDefaultInstance()));
                        pipeline.addLast(new NettyServerHandler());
                    }
                });
        System.out.println("---服务器已准备好.....");
        // 启动服务器  ChannelFuture异步操作后的结果，可以给该对象添加监听事件，监控我们自己关系的事是否成功完成
        ChannelFuture cf = bootstrap.bind(9898).sync();

        // 给ChannelFuture添加一个监听器
        cf.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (cf.isSuccess()){
                    System.out.println("服务器绑定端口成功");
                }else {
                    System.out.println("失败原因:");
                }
            }
        });
        // 对关闭通道进行监听
        cf.channel().closeFuture().sync();
    }

}
