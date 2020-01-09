package com.study.netty.singple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;


/**
 * @Author liaozhenglong
 * @Date 2020/1/3 10:58
 **/
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    /**
     * 处理读业务*/
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //System.out.println("收到客户端ctx----->"+ctx);
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送的消息是:"+buf.toString(CharsetUtil.UTF_8));
        /*Thread.sleep(10*1000);// 模拟耗时操作
        ctx.writeAndFlush(Unpooled.copiedBuffer("延迟10秒后发送消息",CharsetUtil.UTF_8));*/
        // 解决阻塞的问题
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10*1000);// 模拟耗时操作
                    ctx.writeAndFlush(Unpooled.copiedBuffer("延迟10秒后发送消息",CharsetUtil.UTF_8));
                }catch (Exception e){
                    System.out.println("发生了异常："+e.getMessage());
                }
            }
        });
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(20*1000);// 模拟耗时操作 并不是20秒后执行，而是要累积前面的时间加上自己休眠的时间
                    ctx.writeAndFlush(Unpooled.copiedBuffer("延迟30秒后发送消息",CharsetUtil.UTF_8));
                }catch (Exception e){
                    System.out.println("发生了异常："+e.getMessage());
                }
            }
        });

        // 把任务提交到scheduleTaskQueue
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                ctx.writeAndFlush(Unpooled.copiedBuffer("把任务提交到scheduleTaskQueue",CharsetUtil.UTF_8));
            }
        },5, TimeUnit.SECONDS);
        System.out.println("继续...");

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
