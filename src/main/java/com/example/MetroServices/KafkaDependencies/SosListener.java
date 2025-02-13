package com.example.MetroServices.KafkaDependencies;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SosListener {

    @Autowired
    private ObjectMapper objectMapper;
    @KafkaListener(topics = "sos-topic", groupId = "sos-group")
    public void listen(ConsumerRecord<String, String> record) {
        try {
            // Deserialize JSON message into a Map
            Map<String, String> messageData = objectMapper.readValue(record.value(), new TypeReference<Map<String, String>>() {});

            // Extract contact info
            String contact = messageData.get("contact");

            // Print the received data
            System.out.println("Received SOS Alert:");
            System.out.println("Nearest Manager Contact: " + contact);

        } catch (JsonProcessingException e) {
            System.err.println("Error parsing SOS message: " + e.getMessage());
        }
    }

}
