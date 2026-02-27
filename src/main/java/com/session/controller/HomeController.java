package com.session.controller;

import com.session.utils.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public String baseUrl(Authentication authentication, HttpServletRequest request) {
        return SecurityUtil.getUserInfoMessage(authentication, request);
    }

    @GetMapping("/home")
    public String home(Authentication authentication, HttpServletRequest request) {
        return SecurityUtil.getUserInfoMessage(authentication, request);
    }

    @GetMapping("/common")
    public String common(Authentication authentication, HttpServletRequest request) {
        return SecurityUtil.getUserInfoMessage(authentication, request);
    }

    @GetMapping("/sa")
    public String sa(Authentication authentication, HttpServletRequest request) {
        return SecurityUtil.getUserInfoMessage(authentication, request);
    }

    @GetMapping("/ad")
    public String ad(Authentication authentication, HttpServletRequest request) {
        return SecurityUtil.getUserInfoMessage(authentication, request);
    }

    @GetMapping("/op")
    public String op(Authentication authentication, HttpServletRequest request) {
        return SecurityUtil.getUserInfoMessage(authentication, request);
    }

    @GetMapping("/test")
    public String test(Authentication authentication, HttpServletRequest request) {
        return SecurityUtil.getUserInfoMessage(authentication, request);
    }

}
