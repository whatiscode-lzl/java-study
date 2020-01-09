package com.study.netty.groupChat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author liaozhenglong
 * @Date 2020/1/6 14:16
 **/
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    // 声明一个全局变量用于管理上线的用户 GlobalEventExecutor全局事件执行器
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 有新用户连接建立 , 一旦有新用户连接服务器，该方法第一个被触发执行
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush(sdf.format(new Date())+"[新用户]"+channel.remoteAddress()+"加入了聊天\n");
        channelGroup.add(channel);
    }

    // channel断开连接
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //channelGroup.remove(channel); 触发该方法时，会自动删除
        channelGroup.writeAndFlush(sdf.format(new Date())+"用户:"+channel.remoteAddress()+"断开连接\n");
    }

    // 表示channel处于活动状态
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(sdf.format(new Date())+"用户:"+ctx.channel().remoteAddress()+"处于活动状态");
    }

    // 表示channel不是处于活动状态
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(sdf.format(new Date())+"用户:"+ctx.channel().remoteAddress()+"下线了");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.forEach(ch -> {
            if (channel != ch){
                ch.writeAndFlush(sdf.format(new Date())+"客户->"+channel.remoteAddress()+"说:"+msg+"\n");
            }else {
                channel.writeAndFlush(sdf.format(new Date())+"你发了:"+msg+"\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
