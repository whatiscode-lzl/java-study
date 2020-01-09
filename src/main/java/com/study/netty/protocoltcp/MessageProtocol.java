package com.study.netty.protocoltcp;

/**
 * @Author liaozhenglong
 * @Date 2020/1/8 9:20
 * 自定义数据传输协议
 **/

public class MessageProtocol {
    private int len; // 协议的关键属性
    private byte[] content; // 传输的内容

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
