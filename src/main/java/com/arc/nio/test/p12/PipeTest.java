package com.arc.nio.test.p12;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 * @author may
 * @since 2019/12/17 15:12
 */
public class PipeTest {
/*

    Java NIO 管道是两个线程之间的 单项数据连接
    Pipe有一个source通道和一个sink通道。
    数据会被写到sink通道，从source通道读取

*/

    @Test
    public void test1() throws IOException {

        //1、获取管道
        Pipe pipe = Pipe.open();

        //2、将缓冲区中的数据写入管道
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        Pipe.SinkChannel sinkChannel = pipe.sink();

        buffer.put("    数据会被写到sink通道".getBytes());
        buffer.flip();
        sinkChannel.write(buffer);

        //3、数据读取到缓冲区
        Pipe.SourceChannel sourceChannel = pipe.source();
        buffer.flip();
        int len = sourceChannel.read(buffer);
        System.out.println(new String (buffer.array(),0,len));

        sinkChannel.close();
        sourceChannel.close();
    }


}
