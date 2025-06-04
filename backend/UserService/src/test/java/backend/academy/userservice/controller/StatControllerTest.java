package backend.academy.userservice.controller;

import backend.academy.userservice.dto.StatCounterWithoutUserDto;
import backend.academy.userservice.dto.StatDtoCounter;
import backend.academy.userservice.service.StatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StatController.class)
class StatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StatService statService;

    private List<StatDtoCounter> testStats;
    private List<StatCounterWithoutUserDto> testUserStats;

    @BeforeEach
    void setUp() {
        testStats = Arrays.asList(
            StatDtoCounter.builder()
                .username("user1")
                .topic("TEST_CATEGORY")
                .score(5L)
                .build(),
            StatDtoCounter.builder()
                .username("user2")
                .topic("TEST_CATEGORY")
                .score(3L)
                .build()
        );

        testUserStats = Arrays.asList(
            StatCounterWithoutUserDto.builder()
                .topic("TEST_CATEGORY")
                .score(5L)
                .allQuestions(10L)
                .build(),
            StatCounterWithoutUserDto.builder()
                .topic("TEST_CATEGORY")
                .score(3L)
                .allQuestions(6L)
                .build()
        );
    }

    @Test
    void getAllStatsShouldReturnListOfStats() throws Exception {
        when(statService.getAllStats()).thenReturn(testStats);

        mockMvc.perform(get("/getStats"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].username").value("user1"))
            .andExpect(jsonPath("$[0].topic").value("TEST_CATEGORY"))
            .andExpect(jsonPath("$[0].score").value(5))
            .andExpect(jsonPath("$[1].username").value("user2"))
            .andExpect(jsonPath("$[1].topic").value("TEST_CATEGORY"))
            .andExpect(jsonPath("$[1].score").value(3));
    }

    @Test
    void getUserStatsShouldReturnUserStats() throws Exception {
        String username = "user1";
        when(statService.getUserStats(username)).thenReturn(testUserStats);

        mockMvc.perform(get("/getUserStat")
                .param("username", username))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].topic").value("TEST_CATEGORY"))
            .andExpect(jsonPath("$[0].score").value(5))
            .andExpect(jsonPath("$[0].allQuestions").value(10))
            .andExpect(jsonPath("$[1].topic").value("TEST_CATEGORY"))
            .andExpect(jsonPath("$[1].score").value(3))
            .andExpect(jsonPath("$[1].allQuestions").value(6));
    }
}