package com.study.netty.ws;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 * @Author liaozhenglong
 * @Date 2020/1/6 16:47
 **/
public class WSWebSocketFramHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress()+"说:"+msg.text());
        channel.writeAndFlush(new TextWebSocketFrame(LocalDateTime.now()+msg.text()+"\n"));
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户:"+ctx.channel().id().asLongText());
        System.out.println("客户短id:"+ctx.channel().id().asShortText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("断开连接的客户:"+ctx.channel().id().asLongText());
    }
}
