package com.study.netty.protocoltcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

/**
 * @Author liaozhenglong
 * @Date 2020/1/7 17:56
 **/
public class ClientTCPHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
       /* byte[] buffer = new byte[msg.readableBytes()];
        msg.readBytes(buffer);

        System.out.println("收到客户端的消息:"+new String(buffer));*/
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i<10;i++){
            String msg = "今天天气很好，出去散步"+i;
            byte[] content = msg.getBytes(Charset.forName("utf-8"));
            int len = content.length;

            // 创建协议数据包
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(len);
            messageProtocol.setContent(content);

            ctx.writeAndFlush(messageProtocol);
        }
    }
}
