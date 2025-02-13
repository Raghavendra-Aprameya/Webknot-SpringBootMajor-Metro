package com.example.MetroServices.Services;

import com.example.MetroServices.DTO.CheckInDTO;
import com.example.MetroServices.DTO.CheckOutDTO;
import com.example.MetroServices.Entity.CheckInOutEntity;
import com.example.MetroServices.Entity.FareEntity;
import com.example.MetroServices.Entity.StationEntity;
import com.example.MetroServices.Repository.CheckInOutRepository;
import com.example.MetroServices.Repository.FareRepository;
import com.example.MetroServices.Repository.StationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    private final RestTemplate restTemplate;

    @Autowired
    private FareRepository fareRepository;

    private static final String USER_SERVICE_URL = "http://localhost:8080/api/v1/users/validate-card/";

    @Autowired
    public MetroService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String checkIn(CheckInDTO checkInDTO) {
        Long userId = checkInDTO.getUserId();
        Long stationId = checkInDTO.getStationId();

        // ✅ Step 1: Validate Metro Card or QR Code
        ResponseEntity<Boolean> response = restTemplate.getForEntity(USER_SERVICE_URL + userId, Boolean.class);

        if (response.getStatusCode() != HttpStatus.OK || !Boolean.TRUE.equals(response.getBody())) {
            return "Check-in failed: Invalid QR code or insufficient balance!";
        }

        // ✅ Step 2: Validate Station ID
        Optional<StationEntity> stationOpt = stationRepository.findById(stationId);
        if (stationOpt.isEmpty()) {
            return "Invalid station ID!";
        }
        StationEntity sourceStation = stationOpt.get();

        // ✅ Step 3: Fetch Existing Check-in or Create New Entry
        Optional<CheckInOutEntity> existingCheckIn = checkInOutRepository.findById(userId);

        CheckInOutEntity checkIn;
        if (existingCheckIn.isPresent()) {
            checkIn = existingCheckIn.get();  // Update existing entry
        } else {
            checkIn = new CheckInOutEntity(); // Create new entry
            checkIn.setId(userId);
        }

        // ✅ Step 4: Save Check-in Time for Penalty Calculation
        checkIn.setSourceStation(sourceStation);
        checkIn.setCheckInTime(LocalDateTime.now()); // Save check-in time
        checkIn.setCheckOutTime(null);  // Reset checkout time
        checkIn.setDestinationStation(null);
        checkIn.setFare(0.0);

        checkInOutRepository.save(checkIn);

        return "Check-in successful!";
    }
    public String checkOut(CheckOutDTO checkOutPayload) {
        Long userId = checkOutPayload.getUserId();
        String destinationStationName = checkOutPayload.getDestinationStation(); // Station name

        // ✅ Step 1: Fetch User's Last Check-in Data
        Optional<CheckInOutEntity> userCheckInOpt = checkInOutRepository.findById(userId);
        if (userCheckInOpt.isEmpty()) {
            return "Check-out failed: No active check-in found!";
        }

        CheckInOutEntity userData = userCheckInOpt.get();
        if (userData.getCheckInTime() == null || userData.getSourceStation() == null) {
            return "Check-out failed: Missing check-in details!";
        }

        // ✅ Step 2: Fetch Destination Station By Name
        Optional<StationEntity> destinationStationOpt = stationRepository.findByName(destinationStationName);
        if (destinationStationOpt.isEmpty()) {
            return "Check-out failed: Invalid destination station!";
        }

        StationEntity destinationStation = destinationStationOpt.get();
        StationEntity sourceStation = userData.getSourceStation();

        // ✅ Step 3: Fetch Fare From Database
        Optional<FareEntity> fareOpt = fareRepository.findByFromStationAndToStation(sourceStation, destinationStation);
        if (fareOpt.isEmpty()) {
            return "Check-out failed: Fare data not found!";
        }

        double fare = fareOpt.get().getFareAmount();

        // ✅ Step 4: Save Checkout Details
        LocalDateTime checkOutTime = LocalDateTime.now();
        userData.setCheckOutTime(checkOutTime);
        userData.setDestinationStation(destinationStation);
        userData.setFare(fare);
        checkInOutRepository.save(userData);

        return "Check-out successful! Fare: " + fare;
        /*

- Calculates fare based on distance and time taken.
- Sends the final fare to the Payment Service.

*/
//long distance = Math.abs(destinationStation.getId() - userData.getSourceStation().getId());
//        double fare = calculateFare(distance, travelDurationMinutes);
        // ✅ Step 3: Calculate Fare Based on Distance & Time
//        LocalDateTime checkInTime = userData.getCheckInTime();
//        LocalDateTime checkOutTime = LocalDateTime.now();
//        long travelDurationMinutes = java.time.Duration.between(checkInTime, checkOutTime).toMinutes();
    }
    @Cacheable(value = "activeStations", key = "all_active")
    public List<StationEntity> activeStations()
    {
       return stationRepository.findByActiveTrue();




    }

}
