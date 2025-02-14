package com.example.MetroServices.Services;

import com.example.MetroServices.DTO.CheckInDTO;
import com.example.MetroServices.DTO.CheckOutDTO;
import com.example.MetroServices.DTO.SosDTO;
import com.example.MetroServices.Entity.CheckInOutEntity;
import com.example.MetroServices.Entity.FareEntity;
import com.example.MetroServices.Entity.SOSAlertEntity;
import com.example.MetroServices.Entity.StationEntity;
import com.example.MetroServices.Repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

//@Service
//@Slf4j
//public class MetroService {

//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate; // Ensure type matches producer config
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//  public void checkIn(CheckInDTO checkInPayload) {
//        try {
//            String message = objectMapper.writeValueAsString(checkInPayload);
//            kafkaTemplate.send("check-in-topic", message)
//                    .whenComplete((result, ex) -> {
//                        if (ex == null) {
//                            log.info("Message sent successfully to topic: {}", "check-in-topic");
//                        } else {
//                            log.error("Failed to send message to topic: {}", "check-in-topic", ex);
//                        }
//                    });
//        } catch (JsonProcessingException e) {
//            log.error("Error converting DTO to string", e);
//        }
//    }


@Service
@Slf4j
public class MetroService {

    @Autowired
    private CheckInOutRepository checkInOutRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private SosAlertRepository sosAlertRepository;

    private final RestTemplate restTemplate;

    @Autowired
    private FareRepository fareRepository;

    @Autowired
    private StationManagerRepository stationManagerRepository;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate; // Ensure type matches producer config

        private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String USER_SERVICE_URL = "http://localhost:8080/api/v1/users/validate-card/";

    @Autowired
    public MetroService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

//    @Async
//    public CompletableFuture<String> checkIn(CheckInDTO checkInDTO) {
//        return CompletableFuture.supplyAsync(() -> {
//            Long userId = checkInDTO.getUserId();
//            Long stationId = checkInDTO.getStationId();
//
//
//            ResponseEntity<Boolean> response = restTemplate.getForEntity(USER_SERVICE_URL + userId, Boolean.class);
//
//            if (response.getStatusCode() != HttpStatus.OK || !Boolean.TRUE.equals(response.getBody())) {
//                return "Check-in failed: Invalid QR code or insufficient balance!";
//            }
//
//
//            Optional<StationEntity> stationOpt = stationRepository.findById(stationId);
//            if (stationOpt.isEmpty()) {
//                return "Invalid station ID!";
//            }
//            StationEntity sourceStation = stationOpt.get();
//
//
//            Optional<CheckInOutEntity> existingCheckIn = checkInOutRepository.findById(userId);
//
//            CheckInOutEntity checkIn;
//            if (existingCheckIn.isPresent()) {
//                checkIn = existingCheckIn.get();
//            } else {
//                checkIn = new CheckInOutEntity();
//                checkIn.setId(userId);
//            }
//
//            checkIn.setSourceStation(sourceStation);
//            checkIn.setCheckInTime(LocalDateTime.now());
//            checkIn.setCheckOutTime(null);
//            checkIn.setDestinationStation(null);
//            checkIn.setFare(0.0);
//
//            checkInOutRepository.save(checkIn);
//            return "Check-in successful!";
//        });
//    }

    @Transactional
    public String checkIn(CheckInDTO checkInDTO) {
        Long userId = checkInDTO.getUserId();
        Long stationId = checkInDTO.getStationId();


        ResponseEntity<Boolean> response = restTemplate.getForEntity(USER_SERVICE_URL + userId, Boolean.class);

        if (response.getStatusCode() != HttpStatus.OK || !Boolean.TRUE.equals(response.getBody())) {
            return "Check-in failed: Invalid QR code or insufficient balance!";
        }


        Optional<StationEntity> stationOpt = stationRepository.findById(stationId);
        if (stationOpt.isEmpty()) {
            return "Invalid station ID!";
        }
        StationEntity sourceStation = stationOpt.get();


        Optional<CheckInOutEntity> existingCheckIn = checkInOutRepository.findById(userId);

        CheckInOutEntity checkIn;
        if (existingCheckIn.isPresent()) {
            checkIn = existingCheckIn.get();
        } else {
            checkIn = new CheckInOutEntity();
            checkIn.setId(userId);
        }

        checkIn.setSourceStation(sourceStation);
        checkIn.setCheckInTime(LocalDateTime.now());
        checkIn.setCheckOutTime(null);
        checkIn.setDestinationStation(null);
        checkIn.setFare(0.0);

//        checkInOutRepository.save(checkIn);
        return "Check-in successful!";
    }

    public String checkOut(CheckOutDTO checkOutPayload) {
        Long userId = checkOutPayload.getUserId();
        String destinationStationName = checkOutPayload.getDestinationStation(); // Station name


        Optional<CheckInOutEntity> userCheckInOpt = checkInOutRepository.findById(userId);
        if (userCheckInOpt.isEmpty()) {
            return "Check-out failed: No active check-in found!";
        }

        CheckInOutEntity userData = userCheckInOpt.get();
        if (userData.getCheckInTime() == null || userData.getSourceStation() == null) {
            return "Check-out failed: Missing check-in details!";
        }


        Optional<StationEntity> destinationStationOpt = stationRepository.findByName(destinationStationName);
        if (destinationStationOpt.isEmpty()) {
            return "Check-out failed: Invalid destination station!";
        }

        StationEntity destinationStation = destinationStationOpt.get();
        StationEntity sourceStation = userData.getSourceStation();


        Optional<FareEntity> fareOpt = fareRepository.findByFromStationAndToStation(sourceStation, destinationStation);
        if (fareOpt.isEmpty()) {
            return "Check-out failed: Fare data not found!";
        }

        double fare = fareOpt.get().getFareAmount();


        LocalDateTime checkInTime = userData.getCheckInTime();


        LocalDateTime checkOutTime = LocalDateTime.now();
        userData.setCheckOutTime(checkOutTime);
        userData.setDestinationStation(destinationStation);

        long travelDurationMinutes = java.time.Duration.between(checkInTime, checkOutTime).toMinutes();

            if (travelDurationMinutes > 3) {
                Map<String, Object> penaltyMessage = new HashMap<>();
                penaltyMessage.put("userId", userId);
                penaltyMessage.put("penaltyAmount", 50);
                penaltyMessage.put("reason", "Exceeded time limit");

                kafkaTemplate.send("penalty_charged", penaltyMessage.toString());
            }


        userData.setFare(fare);
        checkInOutRepository.save(userData);

        return "Check-out successful! Fare: " + fare;
        /*

- Calculates fare based on distance and time taken.
- Sends the final fare to the Payment Service.

*/
//long distance = Math.abs(destinationStation.getId() - userData.getSourceStation().getId());
//        double fare = calculateFare(distance, travelDurationMinutes);

//        LocalDateTime checkInTime = userData.getCheckInTime();
//        LocalDateTime checkOutTime = LocalDateTime.now();
//        long travelDurationMinutes = java.time.Duration.between(checkInTime, checkOutTime).toMinutes();
    }
    @Cacheable(value = "activeStations", key = "'all_active'")
    public List<StationEntity> activeStations()
    {
       return stationRepository.findByActiveTrue();

    }

    @Cacheable(value = "activeUsers", key = "'all_active_users'")
    public List<CheckInOutEntity> getActiveUsers() {
        System.out.println("Fetching active users from database...");
        return checkInOutRepository.findBySourceStationIsNotNullAndDestinationStationIsNull();
    }

    public void sendNotification(SosDTO sosPayload) {

        Optional<StationEntity> stationOpt = stationRepository.findById(sosPayload.getStationId());
        if (stationOpt.isEmpty()) {
            throw new RuntimeException("Station not found!");
        }



        SOSAlertEntity sosAlertEntity = SOSAlertEntity.builder()
                .userId(sosPayload.getUserId())
                .station(stationOpt.get())
                .resolved(false)
                .build();

        sosAlertRepository.save(sosAlertEntity);

        try {

            Map<String, String> messageData = new HashMap<>();
            messageData.put("contact", "appuraghavendra2003@gmail.com");
            messageData.put("UserId",String.valueOf(sosPayload.getUserId()));

            String message = objectMapper.writeValueAsString(messageData);

            kafkaTemplate.send("sos_triggered", message)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            System.out.println("Message sent successfully to topic: sos-topic");
                        } else {
                            System.err.println("Failed to send message to topic: sos-topic");
                            ex.printStackTrace();
                        }
                    });
        } catch (JsonProcessingException e) {
            System.err.println("Error converting DTO to string: " + e.getMessage());
        }
        //Mail Service needs to be added in Consumer
    }
}


