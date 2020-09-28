package com.leyou.user.service;

import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.utils.CodecUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired private UserMapper userMapper;

    @Autowired private AmqpTemplate amqpTemplate;

    @Autowired private StringRedisTemplate stringRedisTemplate;
    private static final String KEY_PREFIX = "user:verify:";
    public Boolean checkUser(String data, Integer type) {
        User record = new User();
        if(type == 1)record.setUsername(data);
        else if (type == 2)record.setPhone(data);
        else return null;
        return this.userMapper.selectCount(record) == 0;

    }

    public void verifyCode(String phone) {
        if (StringUtils.isBlank(phone))return;
        String code = NumberUtils.generateCode(6);
        System.out.println(code);
        Map<String , String> msg = new HashMap<>();
        msg.put("phone", phone);
        msg.put("code", code);
        //this.amqpTemplate.convertAndSend("LEYOU.SMS.EXCHANGE", "user.verify",msg );
        this.stringRedisTemplate.opsForValue().set(KEY_PREFIX+phone,code, 30, TimeUnit.MINUTES);


    }

    public void register(User user, String code) {
        String s = this.stringRedisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        if (!StringUtils.equals(code,s)){
            return;
        }
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));
        user.setId(null);
        user.setCreated(new Date());
        this.userMapper.insertSelective(user);
        this.stringRedisTemplate.delete(KEY_PREFIX+user.getPhone());



    }

    public User queryUser(String username, String password) {



        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);
        if (user == null) {
            return user;
        }
        password = CodecUtils.md5Hex(password, user.getSalt());
        if (StringUtils.equals(password, user.getPassword())) return user;
        else return null;

    }
}
