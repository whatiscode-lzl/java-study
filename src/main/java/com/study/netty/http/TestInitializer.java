package com.study.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @Author liaozhenglong
 * @Date 2020/1/3 15:36
 **/
public class TestInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 向管道添加处理器
        ChannelPipeline pipeline = ch.pipeline();

        // 添加http的编解码处理器
        pipeline.addLast("myHttpServerCode",new HttpServerCodec());

        // 添加业务处理器
        pipeline.addLast("myHttpServerHandler",new TestHttpServerHandler());

        System.out.println("is ok.......");
    }
}
