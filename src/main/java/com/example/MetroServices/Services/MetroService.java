package com.example.MetroServices.Services;

import com.example.MetroServices.DTO.CheckInDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MetroService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate; // Ensure type matches producer config

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void checkIn(CheckInDTO checkInPayload) {
        try {
            String message = objectMapper.writeValueAsString(checkInPayload);
            kafkaTemplate.send("check-in-topic", message)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("Message sent successfully to topic: {}", "check-in-topic");
                        } else {
                            log.error("Failed to send message to topic: {}", "check-in-topic", ex);
                        }
                    });
        } catch (JsonProcessingException e) {
            log.error("Error converting DTO to string", e);
        }
    }

}
