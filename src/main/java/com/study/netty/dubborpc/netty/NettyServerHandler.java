package com.study.netty.dubborpc.netty;

import com.study.netty.dubborpc.provider.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author liaozhenglong
 * @Date 2020/1/8 14:56
 **/
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String msgs =(String)msg;
        System.out.println("NettyServerHandler--"+msg);
        // 自定义数据协议 "Hho#实际的数据"
        if (msgs.startsWith("Hho#")){
            System.out.println("#的位置=="+msgs.indexOf("#"));
            String subs = msgs.substring(msgs.indexOf("#") + 1);
            ctx.writeAndFlush(new HelloServiceImpl().hello(subs));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
