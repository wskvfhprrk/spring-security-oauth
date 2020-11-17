package com.hejz.resourceserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/public")
    public String hollo(){
        return "公共方法";
    }

    @GetMapping("/private")
    public String hollo2(){
        return "授权才能够访问的方法";
    }
}
