package backend.academy.modelservice.service;

import backend.academy.modelservice.dto.QuestionDto;
import backend.academy.modelservice.exception.QuestionDoesntExists;
import backend.academy.modelservice.model.Question;
import backend.academy.modelservice.repository.QuestionRepository;
import backend.academy.modelservice.service.impl.QuestionServiceImpl;
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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionServiceImpl questionService;

    private Question testQuestion;
    private QuestionDto testQuestionDto;

    @BeforeEach
    void setUp() {
        testQuestion = Question.builder()
                .questionId(1L)
                .topic("Java")
                .question("What is JVM?")
                .correctAnswer("Java Virtual Machine")
                .option1("Java Virtual Machine")
                .option2("Java Visual Machine")
                .option3("Java Verified Machine")
                .option4("Java Variable Machine")
                .build();

        testQuestionDto = QuestionDto.builder()
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
    }

    @Test
    void shouldFindQuestionByTopicAndExcludeIds() {
        when(questionRepository.findFirstByTopicAndQuestionIdNotIn(anyString(), anyList()))
                .thenReturn(Optional.of(testQuestion));

        QuestionDto result = questionService.findQuestionAndTopicWhereIdNotIn(
                Arrays.asList(2L, 3L),
                "Java"
        );

        assertThat(result).isNotNull();
        assertThat(result.getTopic()).isEqualTo("Java");
        assertThat(result.getQuestion()).isEqualTo("What is JVM?");
        assertThat(result.getOptions()).hasSize(4);
        
        verify(questionRepository).findFirstByTopicAndQuestionIdNotIn("Java", Arrays.asList(2L, 3L));
    }

    @Test
    void shouldCreateQuestion() {
        when(questionRepository.save(any(Question.class))).thenReturn(testQuestion);

        QuestionDto result = questionService.createQuestion(testQuestionDto);

        assertThat(result).isNotNull();
        assertThat(result.getTopic()).isEqualTo("Java");
        assertThat(result.getQuestion()).isEqualTo("What is JVM?");
        assertThat(result.getOptions()).hasSize(4);
        
        verify(questionRepository).save(any(Question.class));
    }


    @Test
    void shouldThrowExceptionWhenQuestionNotFound() {
        when(questionRepository.findFirstByTopicAndQuestionIdNotIn(anyString(), anyList()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> 
            questionService.findQuestionAndTopicWhereIdNotIn(Arrays.asList(1L, 2L), "Java")
        )
        .isInstanceOf(QuestionDoesntExists.class)
        .hasMessageContaining("doesnt exist");
    }

    @Test
    void shouldThrowExceptionWhenTopicNotFound() {
        when(questionRepository.findFirstByTopic(anyString()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> 
            questionService.findQuestionAndTopicWhereIdNotIn(null, "Python")
        )
        .isInstanceOf(QuestionDoesntExists.class)
        .hasMessageContaining("doesnt exist");
    }
} 