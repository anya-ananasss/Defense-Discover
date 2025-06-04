package backend.academy.userservice.controller;


import backend.academy.userservice.dto.WavesDto;
import backend.academy.userservice.service.WaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WaveController {

    private final WaveService waveService;

    @GetMapping("/addWave")
    public void addWave(@RequestParam(name = "email") String email,
                        @RequestParam(name = "count") int count){
        waveService.upsertWaves(email, count);
    }

    @GetMapping("/getAllWaves")
    public ResponseEntity<List<WavesDto>> addWave(){
        return ResponseEntity.ok(waveService.getAllWavesDesc());
    }


}
