package backend.academy.apigateway.controller;

import backend.academy.apigateway.client.WaveClient;
import backend.academy.apigateway.dto.WaveDto;
import backend.academy.apigateway.dto.WavesDto;
import backend.academy.apigateway.utils.ApiPaths;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Controller
@RestController
@RequiredArgsConstructor
@Tag(name = "Волны", description = "Учет пройденных волн")
public class WaveController {

    private final WaveClient waveClient;

    @Operation(summary = "Сделать запись о том что игрок прошел волну")
    @PostMapping(ApiPaths.USER_API + "/addWave")
    public ResponseEntity<Void> addWaveInfo(@RequestBody WaveDto waveDto,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Пользователь " + userDetails.getUsername() + " прошел " + waveDto.getWaveCount() + "волн");
        try {
            waveClient.addWave(userDetails.getUsername(), Integer.parseInt(waveDto.getWaveCount()));
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().build();
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Загрузка лидерборда по числу пройденных волн")
    @GetMapping(ApiPaths.BASE_API + "/getWavesLeaderBoard")
    public ResponseEntity<List<WavesDto>> getWaveLeaderBoard() {
        try {
            return ResponseEntity.ok(waveClient.getAllWaves());
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }
}
