package backend.academy.modelservice.service;

import backend.academy.modelservice.client.PromtClient;
import backend.academy.modelservice.dto.*;
import backend.academy.modelservice.exception.QuestionDoesntExists;
import backend.academy.modelservice.service.impl.PromtServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromtServiceTest {

    @Mock
    private PromtClient promtClient;

    @Mock
    private RedisService redisService;

    @Mock
    private QuestionService questionService;

    @Mock
    private KafkaClientService kafkaClientService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private PromtServiceImpl promtService;

    private RequestPromtDto requestPromtDto;
    private PostAnswerDto postAnswerDto;
    private QuestionDto questionDto;
    private CheckAnswerDto checkAnswerDto;
    private PromtDto promtDto;

    @BeforeEach
    void setUp() {
        NumPromtDto numPromtDto = NumPromtDto.builder()
                .topic("Java")
                .numQuestions(1)
                .difficulty(1)
                .keyWords(Arrays.asList("basics", "jvm"))
                .build();

        requestPromtDto = RequestPromtDto.builder()
                .username("testUser")
                .promt(numPromtDto)
                .build();

        promtDto = PromtDto.builder()
                .topic("Java")
                .numQuestions(1)
                .difficulty("средний")
                .keyWords(Arrays.asList("basics", "jvm"))
                .build();

        questionDto = QuestionDto.builder()
                .questionId("1")
                .topic("Java")
                .question("What is JVM?")
                .correctAnswer("Java Virtual Machine")
                .options(Arrays.asList(
                        "Java Virtual Machine",
                        "Java Visual Machine",
                        "Java Verified Machine",
                        "Java Variable Machine"
                ))
                .build();

        postAnswerDto = PostAnswerDto.builder()
                .userName("testUser")
                .questionId("1")
                .answer("Java Virtual Machine")
                .build();

        checkAnswerDto = CheckAnswerDto.builder()
                .isCorrect(true)
                .userName("testUser")
                .correctAnswer("Java Virtual Machine")
                .build();
    }

    @Test
    void shouldGetQuestionFromDatabase() throws Exception {
        when(redisService.getValue(anyString())).thenReturn(null);
        when(questionService.findQuestionAndTopicWhereIdNotIn(any(), anyString()))
                .thenReturn(questionDto);
        when(objectMapper.writeValueAsString(any())).thenReturn("[]");

        List<QuestionDto> result = promtService.getQuestion(requestPromtDto);

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getTopic()).isEqualTo("Java");
        assertThat(result.get(0).getQuestion()).isEqualTo("What is JVM?");
        assertThat(result.get(0).getOptions()).hasSize(4);
        
        verify(redisService).setValue(eq(requestPromtDto.getUsername()), anyString());
        verify(redisService).setValue(eq("currQuest" + requestPromtDto.getUsername()), anyString());
    }

    @Test
    void shouldGetQuestionFromPromtService() throws Exception {
        when(redisService.getValue(anyString())).thenReturn(null);
        when(questionService.findQuestionAndTopicWhereIdNotIn(any(), anyString()))
                .thenThrow(new QuestionDoesntExists("No question found"));
        when(promtClient.getQuestion(any(PromtDto.class)))
                .thenReturn(Arrays.asList(questionDto));
        when(questionService.createQuestion(any(QuestionDto.class)))
                .thenReturn(questionDto);
        when(objectMapper.writeValueAsString(any())).thenReturn("[]");

        List<QuestionDto> result = promtService.getQuestion(requestPromtDto);

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getTopic()).isEqualTo("Java");
        assertThat(result.get(0).getQuestion()).isEqualTo("What is JVM?");
        assertThat(result.get(0).getOptions()).hasSize(4);
        
        verify(redisService).setValue(eq(requestPromtDto.getUsername()), anyString());
        verify(redisService).setValue(eq("currQuest" + requestPromtDto.getUsername()), anyString());
    }

    @Test
    void shouldCheckCorrectAnswer() throws Exception {
        String questionJson = "{\"questionId\":\"1\",\"topic\":\"Java\",\"question\":\"What is JVM?\",\"correctAnswer\":\"Java Virtual Machine\",\"options\":[\"Java Virtual Machine\",\"Java Visual Machine\",\"Java Verified Machine\",\"Java Variable Machine\"]}";
        when(redisService.getValue("currQuest" + postAnswerDto.getUserName()))
                .thenReturn(questionJson);
        when(objectMapper.readValue(questionJson, QuestionDto.class))
                .thenReturn(questionDto);

        CheckAnswerDto result = promtService.postAnswer(postAnswerDto);

        assertThat(result).isNotNull();
        assertThat(result.isCorrect()).isTrue();
        assertThat(result.getCorrectAnswer()).isEqualTo("Java Virtual Machine");
        
        verify(kafkaClientService).sendNotificationStackOverflow(any(StatEvent.class));
        verify(redisService).deleteKey("currQuest" + postAnswerDto.getUserName());
    }

    @Test
    void shouldCheckIncorrectAnswer() throws Exception {
        String questionJson = "{\"questionId\":\"1\",\"topic\":\"Java\",\"question\":\"What is JVM?\",\"correctAnswer\":\"Java Virtual Machine\",\"options\":[\"Java Virtual Machine\",\"Java Visual Machine\",\"Java Verified Machine\",\"Java Variable Machine\"]}";
        when(redisService.getValue("currQuest" + postAnswerDto.getUserName()))
                .thenReturn(questionJson);
        when(objectMapper.readValue(questionJson, QuestionDto.class))
                .thenReturn(questionDto);

        postAnswerDto.setAnswer("Wrong Answer");
        CheckAnswerDto result = promtService.postAnswer(postAnswerDto);

        assertThat(result).isNotNull();
        assertThat(result.isCorrect()).isFalse();
        assertThat(result.getCorrectAnswer()).isEqualTo("Java Virtual Machine");
        
        verify(kafkaClientService).sendNotificationStackOverflow(any(StatEvent.class));
        verify(redisService).deleteKey("currQuest" + postAnswerDto.getUserName());
    }
} 