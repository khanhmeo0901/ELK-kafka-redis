package com.example.demo.service;

import com.example.demo.Constant.Constant;
import com.example.demo.entity.User;
import com.example.demo.Util.ExcelUtil;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class KafkaService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private UserRepository userRepository;

    public KafkaService(KafkaTemplate<String, Object> kafkaTemplate, UserRepository userRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.userRepository = userRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(KafkaService.class);
    public void sendDataEmployee(MultipartFile file , String typeTopic)
    {
        try{
            InputStream inputStream = file.getInputStream();
            if(typeTopic.contains("user"))
            {
                List<User> userList = ExcelUtil.excelUser(inputStream);
                Gson gson = new Gson();
                String jsonData = gson.toJson(userList);
                kafkaTemplate.send(Constant.TOPIC_USER,jsonData);
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public void sendMessage(String message) {
        kafkaTemplate.send(Constant.TOPIC_USER, message);

    }

    @KafkaListener(topics = Constant.TOPIC_USER, groupId = Constant.GROUP )
    public void listen(String message) throws JsonProcessingException {
        logger.info(String.format("Da nhan duoc tin nhan"));
        ObjectMapper mapper = new ObjectMapper();
        List<User> employeeList = mapper.readValue(message, new TypeReference<List<User>>() {});
        userRepository.saveAll(employeeList);
        logger.info("================= Output message =========================");
        logger.info(message);
        logger.info("=====================End Output ==========================");

    }
}
