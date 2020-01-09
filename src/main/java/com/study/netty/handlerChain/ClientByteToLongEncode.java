package com.study.netty.handlerChain;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author liaozhenglong
 * @Date 2020/1/7 16:09
 **/
public class ClientByteToLongEncode extends MessageToByteEncoder<Long> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
        System.out.println("把数据编码为long类型");
        System.out.println("msg=="+msg);
        out.writeLong(msg);
    }
}
