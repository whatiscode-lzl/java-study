package com.study.netty.codec2;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;


/**
 * @Author liaozhenglong
 * @Date 2020/1/3 10:58
 **/
//public class NettyServerHandler extends ChannelInboundHandlerAdapter {
public class NettyServerHandler extends SimpleChannelInboundHandler<StudentPOJO.MyMessage> {


    /**
     * 处理读业务*/
  /*  @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
       StudentPOJO.Student student = (StudentPOJO.Student) msg;
       System.out.println("客户端发送的信息:id="+student.getId()+" 名字="+student.getName());

    }*/

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, StudentPOJO.MyMessage msg) throws Exception {
        // 根据dataType 来显示不同的信息
        StudentPOJO.MyMessage.DataType dataType = msg.getDataType();
        if (dataType == StudentPOJO.MyMessage.DataType.StudentType){
            StudentPOJO.Student student = msg.getStudent();
            System.out.println("id=="+student.getId()+"---姓名=="+student.getName());
        }else if( dataType == StudentPOJO.MyMessage.DataType.WorkerType){
            StudentPOJO.Worker worker = msg.getWorker();
            System.out.println("年龄=="+worker.getAge()+"---姓名=="+worker.getName());
        }else {
            System.out.println("出现未知的数据结构");
        }
    }

    /**
     * 数据读取完毕*/
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端读取客户端的消息完毕..");
        ctx.writeAndFlush(Unpooled.copiedBuffer("你好，我是收到了你的信息",CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
