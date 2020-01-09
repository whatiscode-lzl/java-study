package com.study.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @Author liaozhenglong
 * @Date 2020/1/8 9:46
 **/
public class MyMessageDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyMessageDecoder 解码器被调用...可读字节数"+in.readableBytes()+"java的int的最大值="+Integer.MAX_VALUE);
        // 先读取数据包的长度
        int len = in.readInt();
        // 读取指定长度的字节
        System.out.println("读取到的字节长度=="+len);
        byte[] content = new byte[len];
        in.readBytes(content);


        // 创建协议包
        MessageProtocol mp = new MessageProtocol();
        mp.setLen(len);
        mp.setContent(content);

        out.add(mp);
    }
}
