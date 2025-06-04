package backend.academy.userservice.service;

import backend.academy.userservice.dto.WavesDto;
import backend.academy.userservice.model.User;
import backend.academy.userservice.model.Waves;
import backend.academy.userservice.repository.UserRepository;
import backend.academy.userservice.repository.WavesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WaveServiceTest {

    @Mock
    private WavesRepository wavesRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private WaveService waveService;

    private User testUser;
    private Waves testWaves;
    private List<Waves> testWavesList;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
            .id(1L)
            .username("testUser")
            .email("test@example.com")
            .build();

        testWaves = Waves.builder()
            .id(1L)
            .user(testUser)
            .countWaves(5)
            .build();

        testWavesList = Arrays.asList(
            testWaves,
            Waves.builder()
                .id(2L)
                .user(User.builder()
                    .id(2L)
                    .username("testUser2")
                    .email("test2@example.com")
                    .build())
                .countWaves(3)
                .build()
        );
    }

    @Test
    void upsertWavesShouldUpdateExistingWavesWhenHigherCount() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(wavesRepository.findByUser(testUser)).thenReturn(Optional.of(testWaves));
        when(wavesRepository.save(any(Waves.class))).thenReturn(testWaves);

        waveService.upsertWaves("test@example.com", 10);

        verify(wavesRepository).save(any(Waves.class));
        assertThat(testWaves.getCountWaves()).isEqualTo(10);
    }

    @Test
    void upsertWavesShouldNotUpdateExistingWavesWhenLowerCount() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(wavesRepository.findByUser(testUser)).thenReturn(Optional.of(testWaves));

        waveService.upsertWaves("test@example.com", 3);

        verify(wavesRepository, never()).save(any(Waves.class));
        assertThat(testWaves.getCountWaves()).isEqualTo(5);
    }

    @Test
    void upsertWavesShouldCreateNewWavesWhenNotExists() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(wavesRepository.findByUser(testUser)).thenReturn(Optional.empty());
        when(wavesRepository.save(any(Waves.class))).thenAnswer(invocation -> invocation.getArgument(0));

        waveService.upsertWaves("test@example.com", 5);

        verify(wavesRepository).save(any(Waves.class));
    }

    @Test
    void upsertWavesShouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> waveService.upsertWaves("nonexistent@example.com", 5))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("User not found");

        verify(wavesRepository, never()).save(any(Waves.class));
    }

    @Test
    void getAllWavesDescShouldReturnSortedWavesList() {
        when(wavesRepository.findAllByOrderByCountWavesDesc()).thenReturn(testWavesList);

        List<WavesDto> result = waveService.getAllWavesDesc();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUsername()).isEqualTo("testUser");
        assertThat(result.get(0).getCountWaves()).isEqualTo(5);
        assertThat(result.get(1).getUsername()).isEqualTo("testUser2");
        assertThat(result.get(1).getCountWaves()).isEqualTo(3);
    }
} 