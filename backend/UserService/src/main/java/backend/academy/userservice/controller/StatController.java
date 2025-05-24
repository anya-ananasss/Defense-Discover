package backend.academy.userservice.controller;


import backend.academy.userservice.dto.StatCounterWithoutUserDto;
import backend.academy.userservice.dto.StatDtoCounter;
import backend.academy.userservice.service.StatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatController {

    private final StatService statService;

    @GetMapping("/getStats")
    public ResponseEntity<List<StatDtoCounter>> stats(){
        return ResponseEntity.ok(statService.getAllStats());
    }

    @GetMapping("/getUserStat")
    public ResponseEntity<List<StatCounterWithoutUserDto>> stats(@RequestParam(name = "username") String username) {
        return ResponseEntity.ok(statService.getUserStats(username));
    }


}
