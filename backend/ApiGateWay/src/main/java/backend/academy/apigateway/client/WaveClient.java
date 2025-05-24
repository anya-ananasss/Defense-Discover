package backend.academy.apigateway.client;

import backend.academy.apigateway.dto.WavesDto;
import backend.academy.apigateway.dto.security.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "wave-client", url = "${user.client.url}")
public interface WaveClient {

    @GetMapping("/addWave")
    void addWave(@RequestParam(name = "email") String email, @RequestParam(name = "count") int count);


    @GetMapping("/getAllWaves")
    List<WavesDto> getAllWaves();
}
