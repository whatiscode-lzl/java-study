package com.study.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @Author liaozhenglong
 * @Date 2020/1/6 16:03
 **/
public class MyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent ise = (IdleStateEvent)evt;
            String evenType = "";
            switch (ise.state()){
                case READER_IDLE:
                    evenType = "没有写事件";
                    break;
                case WRITER_IDLE:
                    evenType = "没有读事件";
                    break;
                case ALL_IDLE:
                    evenType = "没有读写事件";
            }

            System.out.println(ctx.channel().remoteAddress()+"----"+evenType+"----");
        }
    }
}
