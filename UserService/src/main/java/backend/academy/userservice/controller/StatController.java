package backend.academy.userservice.controller;


import backend.academy.userservice.dto.StatDtoCounter;
import backend.academy.userservice.service.StatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatController {

    private final StatService statService;

    @PostMapping("/getStats")
    public ResponseEntity<List<StatDtoCounter>> stats(){
        return ResponseEntity.ok(statService.getAllStats());
    }

}
