package com.huban.psb;

import com.alibaba.fastjson.JSONObject;
import com.huban.psb.netty.Message;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName Test
 * @Description TODO
 * Author huihui
 * Date 19-4-15 上午10:08
 * Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {

    /*@Autowired
    private RedisTemplate template;*/


    @org.junit.Test
    public void contextLoads() {
        //RedisTemplate template = SpringContextUtil.getBean("redisTemplate");
        Message msg = new Message();
        msg.setContent("我是要发送的内容");
        msg.setType(1);
        msg.setFrom("APP_123");
        msg.setSubject("我是主题一");
        msg.setTo(new String[]{"1111","2222"});
        String s = JSONObject.toJSONString(msg);
        System.out.println(s);
        Message user1 = JSONObject.parseObject(s, Message.class);

        //template.opsForValue().set("key",user);
        //User key = (User) template.opsForValue().get("key");
        for (String str: user1.getTo()
             ) {
            System.out.println(str);
        }
        //System.out.println(user1.getType());
    }
}
