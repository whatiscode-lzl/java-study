package com.study.netty.ws;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @Author liaozhenglong
 * @Date 2020/1/6 16:26
 **/
public class MyServer {

    public static void main(String[] args) throws InterruptedException {

        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        // 添加http的解码器
                        pipeline.addLast(new HttpServerCodec());
                        // http是以块的方式写,添加ChunkWriterHandler
                        pipeline.addLast(new ChunkedWriteHandler());
                        // 添加用于聚合http传输过程中分段的数据
                        pipeline.addLast(new HttpObjectAggregator(8192));
                        // 浏览器请求 ws://localhost:9898/hello 请求资源的路径
                        //WebSocketServerProtocolHandler核心功能是将 http协议升级为ws协议
                        pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
                        // 处理业务的自定义的handler
                        pipeline.addLast(new WSWebSocketFramHandler());
                    }
                });
        System.out.println("ws 服务器准备好....");
        ChannelFuture future = serverBootstrap.bind(9898).sync();
        future.channel().closeFuture().sync();
    }
}
