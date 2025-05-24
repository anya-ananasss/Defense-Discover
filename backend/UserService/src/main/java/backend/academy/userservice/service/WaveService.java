package backend.academy.userservice.service;

import backend.academy.userservice.dto.WavesDto;
import backend.academy.userservice.model.User;
import backend.academy.userservice.model.Waves;
import backend.academy.userservice.repository.UserRepository;
import backend.academy.userservice.repository.WavesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WaveService {

    private final WavesRepository wavesRepo;
    private final UserRepository userRepo;


    @Transactional
    public void upsertWaves(String email, int newCount) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        wavesRepo.findByUser(user)
                .map(waves -> {
                    if (newCount > waves.getCountWaves()) {
                        waves.setCountWaves(newCount);
                        return wavesRepo.save(waves);
                    }
                    return waves;
                })
                .orElseGet(() -> {
                    Waves waves = Waves.builder()
                            .user(user)
                            .countWaves(newCount)
                            .build();
                    return wavesRepo.save(waves);
                });
    }

    @Transactional(readOnly = true)
    public List<WavesDto> getAllWavesDesc() {
        return wavesRepo.findAllByOrderByCountWavesDesc().stream().map(
                waves -> WavesDto
                        .builder()
                        .username(waves.getUser().getUsername())
                        .countWaves(waves.getCountWaves())
                        .build()).collect(Collectors.toList());
    }
}
