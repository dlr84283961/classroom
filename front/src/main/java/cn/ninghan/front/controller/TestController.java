package cn.ninghan.front.controller;

import cn.ninghan.front.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller(value = "frontTest")
@RequestMapping("/")
@Slf4j
public class TestController {
    @Resource
    private RedisUtil redisUtil;
    @RequestMapping("/test")
    @ResponseBody
    public String testAdd(){
        redisUtil.setAddMembers("a","1","2");
        Long num = redisUtil.setAddMembers("a","1","3");
        return num.toString();
    }
}
