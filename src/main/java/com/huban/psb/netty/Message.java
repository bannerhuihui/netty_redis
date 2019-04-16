package com.huban.psb.netty;

/**
 * @ClassName Message
 * @Description TODO
 * Author huihui
 * Date 19-4-16 上午10:52
 * Version 1.0
 */
public class Message {

    private String from;  //从哪里来的消息
    private String[] to;  //要发送到哪里
    private String subject;  //主题 1 发送QQ消息 2 接收QQ消息 3 发送QQ群列表 4 接收QQ群列表
    private String content;  //内容
    private int type; //消息类型  1 2 3 4
    private String key; //传递netty的key

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
