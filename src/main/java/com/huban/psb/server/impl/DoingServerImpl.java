package com.huban.psb.server.impl;

import com.huban.psb.server.DoingServer;
import org.springframework.stereotype.Service;

@Service
public class DoingServerImpl implements DoingServer {
    @Override
    public String doServer() {
        return "我是测试数据";
    }
}
