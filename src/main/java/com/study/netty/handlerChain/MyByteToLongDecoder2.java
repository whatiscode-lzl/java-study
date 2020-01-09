package com.study.netty.handlerChain;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @Author liaozhenglong
 * @Date 2020/1/7 16:59
 **/
public class MyByteToLongDecoder2 extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // ReplayingDecoder内部会自动维护字节数的问题
        out.add(in.readLong());
    }
}
