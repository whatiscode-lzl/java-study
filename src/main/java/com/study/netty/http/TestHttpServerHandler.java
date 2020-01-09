package com.study.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.net.URI;
import java.nio.charset.Charset;

/**
 * @Author liaozhenglong
 * @Date 2020/1/3 15:35
 *
 * SimpleChannelInboundHandler 继承 ChannelInboundHandlerAdapter
 *
 * 服务器和客户端通信时封装的数据类型HttpObject
 **/
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        // 判断msg是不是HttpRequest
        if (msg instanceof HttpRequest){
            System.out.println("msg的类型:"+msg.getClass());
            System.out.println("客户端的地址:"+ctx.channel().remoteAddress());

            // 过滤某些资源请求
            HttpRequest httpRequest = (HttpRequest)msg;
            URI uri = new URI(httpRequest.getUri());
            if ("/favicon.ico".equals(uri.getPath())){
                System.out.println("客户端请求了不存在的资源");
                return;
            }

            ByteBuf content = Unpooled.copiedBuffer("服务端返回的信息", Charset.forName("GBK"));

            // 创建http返回的协议数据
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());

            // 将协议数据返回
            ctx.writeAndFlush(response);
        }
    }
}
