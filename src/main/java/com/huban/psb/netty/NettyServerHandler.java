package com.huban.psb.netty;

import com.alibaba.fastjson.JSONObject;
import com.huban.psb.netty.util.SpringContextUtil;
import com.huban.psb.redis.PublisherService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class NettyServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>{
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServerHandler.class);

    //记录心跳
    private int lossConnectCount = 0;
    // 记录所有的netty客户端连接
    public static Map<String,ChannelHandlerContext> map = new HashMap<String,ChannelHandlerContext>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = insocket.getAddress().getHostAddress();
        LOGGER.info("NettyServerHandler#channelRead0 接收到客户端"+clientIp+"请求，内容为"+msg.text());
        Message message = null;
        if(msg!=null){
            try{
                message = JSONObject.parseObject(msg.text(), Message.class);
            }catch (Exception e){
                LOGGER.error("消息解析异常！");
            }
            if(StringUtils.equals(message.getContent(),"ping-pong-ping-pang")){
                LOGGER.info("{} -> [心跳监测] {}：通道活跃", this.getClass().getName(), ctx.channel().id());
                // 心跳消息
                lossConnectCount = 0;
                return;
            }
            if(message.getType()==0){
                map.put(message.getFrom(),ctx);
                LOGGER.info("用户登录成功！");
                ctx.channel().writeAndFlush(new TextWebSocketFrame("success"));
            }else{
                this.appAndPsb(ctx,message);
            }
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = insocket.getAddress().getHostAddress();
        LOGGER.info("收到客户端[ip:" + clientIp + "]连接");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 当出现异常就关闭连接
        ctx.close();
    }

    //处理app和psb通信
    public void appAndPsb(ChannelHandlerContext ctx, Message message){
        String error = "";
        if(message!=null){
            //过滤消息
            boolean typesuccess = false;
            if(message.getType()==1||message.getType()==2||message.getType()==3||message.getType()==4){
                typesuccess = true;
            }
            if(StringUtils.isNotEmpty(message.getFrom())&&typesuccess){
                map.put(message.getFrom(),ctx);
            }else {
                error = "{\"error\":\"消息来源为空!\"}";
            }
            if(StringUtils.isEmpty(message.getContent())){
                error = "{\"error\":\"消息体为空!\"}";
            }
            if(StringUtils.isNotEmpty(error)){
                ctx.channel().writeAndFlush(new TextWebSocketFrame(error));
            }
            String[] to = message.getTo();
            for (String toUser: to) {
                if(map.get(toUser) == null){
                    error = "{\"error\":\"连接还没有建立!\"}";
                    ctx.channel().writeAndFlush(new TextWebSocketFrame(error));
                }else{
                    message.setKey(toUser);
                    PublisherService publisherService = SpringContextUtil.getBean("publisherService");
                    publisherService.pubMsg(JSONObject.toJSONString(message));
                }
            }
        }else{
            LOGGER.info("传输的内容为null");
            error = "{\"error\":\"消息内容为空!\"}";
            ctx.channel().writeAndFlush(new TextWebSocketFrame(error));
        }

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                lossConnectCount++;
                if (lossConnectCount > 2) {
                    LOGGER.info("{} -> [释放不活跃通道] {}", this.getClass().getName(), ctx.channel());
                    ctx.channel().close();
                }
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
