package com.study.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

/**
 * @Author liaozhenglong
 * @Date 2020/1/7 17:56
 **/
public class ClientTCPHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] buffer = new byte[msg.readableBytes()];
        msg.readBytes(buffer);

        System.out.println("收到客户端的消息:"+new String(buffer));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i<10;i++){
            ctx.writeAndFlush(Unpooled.copiedBuffer("你好服务端"+(i), Charset.forName("utf-8")));
        }
    }
}
