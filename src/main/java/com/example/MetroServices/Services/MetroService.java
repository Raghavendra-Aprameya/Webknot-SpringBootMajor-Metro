package com.example.MetroServices.Services;

import com.example.MetroServices.DTO.CheckInDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MetroService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate; // Ensure type matches producer config

    public void checkIn(String name) {
        kafkaTemplate.send("check-in-topic", name);
    }

}
