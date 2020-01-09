package com.study.netty.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;


/**
 * @Author liaozhenglong
 * @Date 2020/1/3 10:58
 **/
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    /**
     * 处理读业务*/
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
       StudentPOJO.Student student = (StudentPOJO.Student) msg;
       System.out.println("客户端发送的信息:id="+student.getId()+" 名字="+student.getName());

    }

    /**
     * 数据读取完毕*/
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端读取客户端的消息完毕..");
        ctx.writeAndFlush(Unpooled.copiedBuffer("你好，我是收到了你的信息",CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
