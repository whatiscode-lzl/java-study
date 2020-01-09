package com.study.netty.dubborpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author liaozhenglong
 * @Date 2020/1/8 15:37
 **/
public class NettyClient {

    private static ExecutorService executorService = Executors.newFixedThreadPool(3);

    private static NettyClientHandler client ;

    // 编写方法使用代理模式去获取代理对象
    public Object getBean(final Class<?> serviceClass,final String providerName){
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{serviceClass},(proxy, method, args) -> {
            //System.out.println("获取代理对象..proxy="+proxy);
            if (client == null){
                 initClient();
                System.out.println("---client初始化完毕--");
            }
            // 设置要发给服务器端的消息
             // providerName是协议头 args[]就是客户端调用api的参数
                    client.setPatra(providerName+args[0]);
                    System.out.println("----------------========--------");
                    //Object invoke = method.invoke(new HelloServiceImpl(), args[0]);
                    return executorService.submit(client).get();
                    //return invoke;

            //return client.call();
                });
    }
    private static void initClient() throws InterruptedException {
        client = new NettyClientHandler();
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(client);
                    }
                });
        System.out.println("服务端准备就绪...........");
        ChannelFuture future = bootstrap.connect("127.0.0.1", 9898).sync();
        //future.channel().closeFuture().sync(); // 是阻塞的方法？？？？
        System.out.println("成功连接到服务端.....");
    }
}
