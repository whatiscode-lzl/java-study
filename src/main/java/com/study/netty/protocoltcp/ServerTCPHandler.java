package com.study.netty.protocoltcp;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

/**
 * @Author liaozhenglong
 * @Date 2020/1/7 17:49
 **/
public class ServerTCPHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private int count = 0;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        byte[] buffer = msg.getContent();

        System.out.println("服务器收到客户端的信息"+(++this.count));
        System.out.println("收到客户端的消息:"+new String(buffer));

        ctx.writeAndFlush(Unpooled.copiedBuffer("你好客户端"+this.count, Charset.forName("utf-8")));
    }
}
