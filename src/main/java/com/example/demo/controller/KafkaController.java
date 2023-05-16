package com.example.demo.controller;

import com.example.demo.service.KafkaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/kafka")
public class KafkaController {
    private final KafkaService kafkaProducerService;

    public KafkaController(KafkaService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }
    @PostMapping("/publish")
    public ResponseEntity<String> sendMessage(@RequestParam("file") MultipartFile file, @RequestParam String typeTopic) {
        kafkaProducerService.sendDataEmployee(file,typeTopic);
        return ResponseEntity.ok("Message sent to kafka topic");
    }
}
