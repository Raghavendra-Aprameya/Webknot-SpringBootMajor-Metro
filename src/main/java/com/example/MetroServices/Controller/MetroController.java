package com.example.MetroServices.Controller;


import com.example.MetroServices.DTO.CheckInDTO;
import com.example.MetroServices.Services.MetroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/metro")
public class MetroController {
    @Autowired
    MetroService metroService;



     @PostMapping("/check-in")
    public void checkIn(@RequestBody String payload)
     {
         metroService.checkIn(payload);
     }




}
