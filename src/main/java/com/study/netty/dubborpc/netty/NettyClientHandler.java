package com.study.netty.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 * @Author liaozhenglong
 * @Date 2020/1/8 15:17
 **/
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context;
    private String result; // 返回的结果
    private String patra; // 参数
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接到服务器");
        context = ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 提供者返回的数放在result
        result = msg.toString();

        // 唤醒等待的线程 既唤醒call()
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    // 被代理对象调用，发送数据给服务者 然后等被channelRead唤醒 最后返回结果
    @Override
    public synchronized  Object call() throws Exception {
        System.out.println("执行client的call()");
        context.writeAndFlush(patra);
        wait();
        return result;
    }

    void setPatra(String para){
        this.patra = "Hho#"+para;
    }
}
