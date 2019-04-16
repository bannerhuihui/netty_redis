package com.huban.psb.redis;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @ClassName PublisherService
 * @Description TODO
 * Author huihui
 * Date 19-4-12 上午10:36
 * Version 1.0
 */
@Service
public class PublisherService {

    private static final Logger log = LoggerFactory.getLogger(PublisherService.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private String userpwd;

    public String pubMsg(String msg) {

        if (StringUtils.isNotEmpty(msg)){
            stringRedisTemplate.convertAndSend("HUBANAPPPSB_MSG",msg);
            return "success";
        }else {
            return "no my phone";
        }
    }

}
