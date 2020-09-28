package com.leyou.auth.service;


import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(JwtProperties.class
)
public class AuthService {

    @Autowired private UserClient userClient;
    @Autowired private JwtProperties jwtProperties;

    public String login(String username, String password) {
        User user = this.userClient.queryUser(username, password);
        if (user == null) return null;

        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        try {
            return JwtUtils.generateToken(userInfo, jwtProperties.getPrivateKey(), this.jwtProperties.getExpire());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
