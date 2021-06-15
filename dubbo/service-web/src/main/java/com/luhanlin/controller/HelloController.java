package com.luhanlin.controller;

import com.luhanlin.servive.IRequestIpService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 前端请求接口
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
@Controller
public class HelloController {

    @Reference
    private IRequestIpService requestIpService;

    @RequestMapping("/hello.do")
    public  String  toHello(HttpServletRequest request){
        String hello = requestIpService.outRequestIp("hello");
        System.out.println(hello);
        return "hello";
    }
}
