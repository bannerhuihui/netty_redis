package com.huban.psb.redis;

/**
 * @ClassName SendMsg
 * @Description TODO
 * Author huihui
 * Date 19-4-16 下午1:45
 * Version 1.0
 */
public class SendMsg {

    private String from;  //从哪里来的消息
    private String content;  //内容
    private int type; //消息类型  1 2 3 4

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
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
