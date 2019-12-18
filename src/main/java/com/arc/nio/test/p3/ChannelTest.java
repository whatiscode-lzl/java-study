package com.arc.nio.test.p3;

import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.SortedMap;

/**
 * 测试
 *
 * @author may
 * @since 2019/12/9 10:41
 */
public class ChannelTest {

/*
 一、通道（Channel）：用于源节点与目标节点的连接，在Java NIO中负责数据的传输，Channel本省不存储数据，因此需要配合缓冲区进行传输
 二、通道的主要实现类
 java.nio.channels.Channel 接口     since 1.4
java.nio.channels.ByteChannel

     java.nio.channels.FileChannel
                             |--SocketChannel
                             |--ServerSocketChannel
                             |--DatagramChannel

三、获取通道
    1.Java针对通道的类提供了getChannel（）方法
    本地IO：
    FileInputStream、FileOutStream
    RandomAccessFile

    网络IO：
    Socket
    ServerSocket
    DatagramSocket

    2.在JDK1.7==NIO2 中 静态方法： open()
    3.在JDK1.7==NIO2 中 Files工具类中newByteChannel()

四、通道之间传输数据
    transferTo()
    transferFrom（）

五、分散（Scatter）与聚集（Gather）
    分散读取： 将通道中大数据分散到多个缓冲区中        注意：按照缓冲区的顺序，从channel中读取依次将buffer填满
    聚集写入：将多个缓冲区中的数据聚集到通道中

六、字符集：CharSet
    编码：字符串-->字节数组
    解码：字节数组-->字符串

*/


    public static void main(String[] args) throws IOException {

        new ChannelTest().test1();
        new ChannelTest().test2();
        new ChannelTest().test3();
        new ChannelTest().test4();
        new ChannelTest().test5();

    }

    //字符集
    private void test5() throws CharacterCodingException {
                SortedMap<String, Charset> map = Charset.availableCharsets();
                for (Map.Entry<String, Charset> entry : map.entrySet()) {
                    System.out.println(entry.getKey() + "--" + entry.getValue());
                }


        Charset gbkCharset = Charset.forName("GBK");

        //获取编码器
        CharsetEncoder charsetEncoder = gbkCharset.newEncoder();

       //获取解码器
        CharsetDecoder charsetDecoder = gbkCharset.newDecoder();

        CharBuffer cBuffer = CharBuffer.allocate(1024);
        String msg = "测试测试1234";
        cBuffer.put(msg);

        cBuffer.flip();

        //编码
        ByteBuffer bBuffer = charsetEncoder.encode(cBuffer);

        //看一下
        for (int i = 0; i < msg.length(); i++) {
            System.out.println(bBuffer.get());
        }


        //解码
        bBuffer.flip();
        CharBuffer decodeCharBuffer = charsetDecoder.decode(bBuffer);
        System.out.println(decodeCharBuffer.toString());
        System.out.println("__________________________________");

        Charset charset2 = Charset.forName("UTF-8");
        bBuffer.flip();
        CharBuffer decode2 = charset2.decode(bBuffer);
        System.out.println(decode2.toString());

    }

    //分散（Scatter）与聚集（Gather）
    private void test4() throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("T:/data/nio/1.txt", "rw");

        //1、获取通道
        FileChannel channel1 = randomAccessFile.getChannel();

        //2、分配指定大小的缓冲区
        ByteBuffer buffer1 = ByteBuffer.allocate(100);
        ByteBuffer buffer2 = ByteBuffer.allocate(1024);

        //3、分散读取
        ByteBuffer[] buffers = {buffer1, buffer2};
        channel1.read(buffers);

         //打印看一下
        for (ByteBuffer buffer : buffers) {
            buffer.flip();
        }

        System.out.println(new String(buffers[0].array(), 0, buffers[0].limit()));
        System.out.println("---------------------------------");
        System.out.println(new String(buffers[1].array(), 0, buffers[1].limit()));


        //4、聚集写入
        RandomAccessFile outRandomAccessFile = new RandomAccessFile("T:/data/nio/3.txt", "rw");
        FileChannel channel2 = outRandomAccessFile.getChannel();
        long write = channel2.write(buffers);
        System.out.println(write);

        channel1.close();
        channel2.close();

    }

    private void test3() {
        FileChannel inChannel = null;
        FileChannel outChannel = null;

        try {
            //1、创建通道
            inChannel = FileChannel.open(Paths.get("T:/data/temp/1.jpg"), StandardOpenOption.READ);
            outChannel = FileChannel.open(Paths.get("T:/data/temp/3.jpg"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inChannel != null) {
                try {
                    inChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outChannel != null) {
                try {
                    outChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    //2、利用通道完成文件复制(直接缓冲区--内存映射)
    private void test2() {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            //1、创建通道
            inChannel = FileChannel.open(Paths.get("T:/data/temp/1.jpg"), StandardOpenOption.READ);
            outChannel = FileChannel.open(Paths.get("T:/data/temp/2.jpg"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

            //2、声明内存映射ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
            MappedByteBuffer inMappedByteBuffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
            MappedByteBuffer outMappedByteBuffer = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());


            //3、文件从传输--直接对缓冲区数据进行读写操作
            byte[] dest = new byte[inMappedByteBuffer.limit()];
            inMappedByteBuffer.get(dest);
            outMappedByteBuffer.put(dest);

            //4、关闭流
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (inChannel != null) {
                try {
                    inChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outChannel != null) {
                try {
                    outChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //1、利用通道完成文件复制(非直接缓冲区--JVM 堆内存)
    public void test1() {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        FileChannel inputStreamChannel = null;
        FileChannel outputStreamChannel = null;

        File source = null;
        try {
            source = ResourceUtils.getFile("classpath:static/image/1111.jpg");
            String target = "2.jpg";
            fileInputStream = new FileInputStream(source);
            fileOutputStream = new FileOutputStream(target);

            //1 获取通道
            inputStreamChannel = fileInputStream.getChannel();
            outputStreamChannel = fileOutputStream.getChannel();

            //2 分配指定缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            //3 将通道中的数据存入缓冲区
            while (inputStreamChannel.read(buffer) != -1) {
                buffer.flip();//切换读数据的模式
                 //4 将缓冲区的数据写入通道
                outputStreamChannel.write(buffer);
                buffer.clear();//清空缓存区
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStreamChannel != null) {
                try {
                    inputStreamChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStreamChannel != null) {
                try {
                    outputStreamChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

}
