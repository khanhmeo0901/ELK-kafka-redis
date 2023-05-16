package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;

@Service
public class RedisService {
    private  RedisTemplate<String, User> redisTemplate;

    private UserRepository userRepository;

    public RedisService(RedisTemplate<String, User> redisTemplate, UserRepository userRepository) {
        this.redisTemplate = redisTemplate;
        this.userRepository = userRepository;
    }

    public void saveToRedis(User user) {
        redisTemplate.opsForValue()
                .set(user.getId().toString(), user);

    }
    public List<User> getAllRedis() {
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            saveToRedis(user);
        }
        return userList;
    }




}
