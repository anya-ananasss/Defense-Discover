package backend.academy.userservice.controller;

import backend.academy.userservice.dto.WavesDto;
import backend.academy.userservice.service.WaveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WaveController.class)
class WaveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WaveService waveService;

    private List<WavesDto> testWaves;

    @BeforeEach
    void setUp() {
        testWaves = Arrays.asList(
            WavesDto.builder()
                .username("testUser")
                .countWaves(5)
                .build(),
            WavesDto.builder()
                .username("testUser2")
                .countWaves(3)
                .build()
        );
    }

    @Test
    void addWaveShouldCallService() throws Exception {
        String email = "test@example.com";
        int count = 5;

        mockMvc.perform(get("/addWave")
                .param("email", email)
                .param("count", String.valueOf(count)))
            .andExpect(status().isOk());

        verify(waveService).upsertWaves(email, count);
    }

    @Test
    void getAllWavesShouldReturnListOfWaves() throws Exception {
        when(waveService.getAllWavesDesc()).thenReturn(testWaves);

        mockMvc.perform(get("/getAllWaves"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].username").value("testUser"))
            .andExpect(jsonPath("$[0].countWaves").value(5))
            .andExpect(jsonPath("$[1].username").value("testUser2"))
            .andExpect(jsonPath("$[1].countWaves").value(3));
    }
} 