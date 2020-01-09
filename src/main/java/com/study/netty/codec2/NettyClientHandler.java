package com.study.netty.codec2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Random;

/**
 * @Author liaozhenglong
 * @Date 2020/1/3 11:28
 **/
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当通道准备就绪时触发该方法*/
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端的ctx:"+ctx);
        StudentPOJO.MyMessage myMessage =null;
        int rand = new Random().nextInt(3);
        if (rand == 0){
            myMessage = StudentPOJO.MyMessage.newBuilder().setDataType(StudentPOJO.MyMessage.DataType.StudentType)
                    .setStudent(StudentPOJO.Student.newBuilder().setId(5).setName("学生姓名").build()).build();
        }else {
            myMessage = StudentPOJO.MyMessage.newBuilder().setDataType(StudentPOJO.MyMessage.DataType.WorkerType)
                    .setWorker(StudentPOJO.Worker.newBuilder().setAge(23).setName("工人姓名").build()).build();
        }
        ctx.writeAndFlush(myMessage);
    }

    /**
     * 当有数据可读时触发*/
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead的ctx:"+ctx);
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("收到服务端返回的信息:"+buf.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
    }
}
