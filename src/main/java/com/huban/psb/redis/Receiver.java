package com.huban.psb.redis;

import com.alibaba.fastjson.JSONObject;
import com.huban.psb.netty.Message;
import com.huban.psb.netty.NettyServerHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;

/**
 * MQ消费者
 * @ClassName Receiver
 * @Description TODO
 * Author huihui
 * Date 19-4-12 上午10:22
 * Version 1.0
 */
public class Receiver {
    public static final Logger logger = LoggerFactory.getLogger(Receiver.class);


    private CountDownLatch latch;

    @Autowired
    public Receiver(CountDownLatch latch){
        this.latch = latch;
    }



    public void receiveMessage(String msg){
        logger.info("Receiver:消费的消息为："+msg);
        Message message = JSONObject.parseObject(msg, Message.class);
        String key = message.getKey();
        NettyServerHandler nettyServerHandler = new NettyServerHandler();
        ChannelHandlerContext context = nettyServerHandler.map.get(key);
        SendMsg sendMsg = new SendMsg();
        sendMsg.setContent(message.getContent());
        sendMsg.setFrom(message.getFrom());
        sendMsg.setType(message.getType());
        String sendmsg = JSONObject.toJSONString(sendMsg);
        context.writeAndFlush(new TextWebSocketFrame(sendmsg));
        latch.countDown();
    }


}
