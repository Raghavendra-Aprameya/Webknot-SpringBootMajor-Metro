package com.example.MetroServices.Controller;


import com.example.MetroServices.DTO.CheckInDTO;
import com.example.MetroServices.DTO.CheckOutDTO;
import com.example.MetroServices.Entity.StationEntity;
import com.example.MetroServices.Services.MetroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/metro")
public class MetroController {
    @Autowired
    MetroService metroService;



     @PostMapping("/check-in")
     
     public ResponseEntity<String> checkIn(@RequestBody CheckInDTO checkInDTO) {
         String response = metroService.checkIn(checkInDTO);
         return ResponseEntity.ok(response);
     }
     @PostMapping("/check-out")
     public ResponseEntity<String> checkOut(@RequestBody CheckOutDTO checkOutPayload) {
         String response = metroService.checkOut(checkOutPayload);
         return ResponseEntity.ok(response);
     }

     @GetMapping("/active-stations")
    public ResponseEntity<List<StationEntity>> activeStations()
     {
         return ResponseEntity.ok(metroService.activeStations())
     }


}
