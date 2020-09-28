package com.leyou.user.controller;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired private UserService userService;

    @GetMapping("/check/{data}/{type}")public ResponseEntity<Boolean> checkUser(@PathVariable("data")String data, @PathVariable("type") Integer type){
        Boolean b = this.userService.checkUser(data, type);
        if (b == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(b);
    }

    @PostMapping("code") public ResponseEntity<Void> verifyCode(@RequestParam("phone")String phone){
        this.userService.verifyCode(phone);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("register")public  ResponseEntity<Void> register(@Valid User user, @RequestParam("code")String code){
        this.userService.register(user, code);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("query") public ResponseEntity<User> queryUser(@RequestParam("username")String username,@RequestParam("password")String password){
        User user = this.userService.queryUser(username,password);
        if (user == null){
            return ResponseEntity.status(400).build();
        }return ResponseEntity.ok(user);

    }


}
