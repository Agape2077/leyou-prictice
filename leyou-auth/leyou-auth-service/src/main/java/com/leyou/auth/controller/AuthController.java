package com.leyou.auth.controller;


import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.service.AuthService;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller public class AuthController {

    @Autowired private AuthService authService;
    @Autowired private JwtProperties jwtProperties;

    @PostMapping("login") public ResponseEntity<Void> login (@RequestParam("username")String username, @RequestParam("password") String password, HttpServletRequest request, HttpServletResponse response){
        String token = this.authService.login(username,password);
        if (StringUtils.isBlank(token)) return ResponseEntity.status(400).build();
        CookieUtils.setCookie(request,response, this.jwtProperties.getCookieName(), token, this.jwtProperties.getExpire()*60, "utf-8",true);
        return ResponseEntity.status(200).build();

    }

    @GetMapping("verify") public ResponseEntity<UserInfo> verify(@CookieValue("LY_TOKEN")String token, HttpServletRequest request, HttpServletResponse response){

        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, this.jwtProperties.getPublicKey());
            if (userInfo == null) {
                return ResponseEntity.status(403).build();
            }
            token = JwtUtils.generateToken(userInfo, this.jwtProperties.getPrivateKey(), this.jwtProperties.getExpire());

            CookieUtils.setCookie(request, response, this.jwtProperties.getCookieName(), token, this.jwtProperties.getExpire() * 60 , "utf-8", true);

            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }return ResponseEntity.status(403).build();


    }


}
