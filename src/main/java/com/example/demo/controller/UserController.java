package com.example.demo.controller;



import com.example.demo.Constant.Constant;
import com.example.demo.entity.User;
import com.example.demo.service.ElasticsearchService;
import com.example.demo.service.RedisService;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
   // private final KafkaService kafkaService;
    private final ElasticsearchService elasticsearchService;
    private final RedisService redisService;

    public UserController(UserService userService, ElasticsearchService elasticsearchService , RedisService redisService) {
        this.userService = userService;
        this.elasticsearchService = elasticsearchService;
        this.redisService = redisService;
    }
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user){
        userService.createUser(user);
        return ResponseEntity.ok("User created successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAllUser()
    {
        userService.deleteAllUser();
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/all")
    public ResponseEntity<String> getAllDataFromRedis()
    {
        redisService.getAllRedis();
        return ResponseEntity.ok("ok");
    }
}
