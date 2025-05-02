package backend.academy.modelservice.service.impl;

import backend.academy.modelservice.client.PromtClient;
import backend.academy.modelservice.dto.*;
import backend.academy.modelservice.exception.QuestionDoesntExists;
import backend.academy.modelservice.mapper.QuestionMapper;
import backend.academy.modelservice.service.KafkaClientService;
import backend.academy.modelservice.service.PromtService;
import backend.academy.modelservice.service.QuestionService;
import backend.academy.modelservice.service.RedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromtServiceImpl implements PromtService {

    private final RedisService redisService;
    private final QuestionService questionService;
    private final ObjectMapper objectMapper;
    private final PromtClient promtClient;
    private final KafkaClientService kafkaClientService;

    @Override
    public List<QuestionDto> getQuestion(RequestPromtDto requestPromtDto) {

        try {
            String s = redisService.getValue(requestPromtDto.getUsername());
            List<Long> ids;
            if (s == null) {
                ids = new ArrayList<>();
            } else {
                ids = objectMapper.readValue(s, new TypeReference<List<Long>>() {
                });
            }

            QuestionDto questionDto;
            try {
                questionDto = questionService.findQuestionAndTopicWhereIdNotIn(ids, requestPromtDto.getPromt().getTopic());
            } catch (QuestionDoesntExists e) {
                List<QuestionDto> questionDtos = promtClient.getQuestion(requestPromtDto.getPromt());
                questionDto = questionDtos.get(0);

                questionDto = questionService.createQuestion(questionDto);

            } catch (Exception e) {
                log.error("Модель не доступна");
                questionDto = questionService.findQuestionAndTopicWhereIdNotIn(List.of(), requestPromtDto.getPromt().getTopic());
            }


            ids.add(Long.parseLong(questionDto.getQuestionId()));
            redisService.setValue(requestPromtDto.getUsername(), objectMapper.writeValueAsString(ids));

            redisService.setValue("currQuest" + requestPromtDto.getUsername(), objectMapper.writeValueAsString(questionDto));

            System.out.println("currQuest" + requestPromtDto.getUsername());

            return List.of(questionDto);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CheckAnswerDto postAnswer(PostAnswerDto postAnswerDto) {
        try {
            System.out.println("currQuest" + postAnswerDto.getUserName());
            String question = redisService.getValue("currQuest" + postAnswerDto.getUserName());
            QuestionDto questionDto = objectMapper.readValue(question, QuestionDto.class);
            redisService.deleteKey("currQuest" + postAnswerDto.getUserName());

            if (Objects.equals(questionDto.getCorrectAnswer(), postAnswerDto.getAnswer())) {


                kafkaClientService.sendNotificationStackOverflow(
                        StatEvent
                                .builder()
                                .username(postAnswerDto.getUserName())
                                .topic(questionDto.getTopic())
                                .build()
                );

                return CheckAnswerDto
                        .builder()
                        .isCorrect(true)
                        .userName(postAnswerDto.getUserName())
                        .correctAnswer(postAnswerDto.getAnswer())
                        .build();
            }

            return CheckAnswerDto
                    .builder()
                    .isCorrect(false)
                    .userName(postAnswerDto.getUserName())
                    .correctAnswer(postAnswerDto.getAnswer())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(fixedRate = 10000)
    @Transactional
    public void runtimeQuestionGen() {
        log.info("Запросы генерируются");

        try {


            PromtDto promt = PromtDto
                    .builder().
                    keyWords(List.of()).
                    difficulty("средний")
                    .numQuestions(5)
                    .topic("history")
                    .build();

            List<QuestionDto> questionDtos = promtClient.getQuestion(promt);
            questionDtos.forEach(
                    questionService::createQuestion
            );

            promt = PromtDto
                    .builder().
                    keyWords(List.of()).
                    difficulty("средний")
                    .numQuestions(5)
                    .topic("science")
                    .build();

            questionDtos = promtClient.getQuestion(promt);
            questionDtos.forEach(
                    questionService::createQuestion
            );

            promt = PromtDto
                    .builder().
                    keyWords(List.of()).
                    difficulty("средний")
                    .numQuestions(5)
                    .topic("culture")
                    .build();

            questionDtos = promtClient.getQuestion(promt);
            questionDtos.forEach(
                    questionService::createQuestion
            );


            promt = PromtDto
                    .builder().
                    keyWords(List.of()).
                    difficulty("средний")
                    .numQuestions(5)
                    .topic("nature")
                    .build();

            questionDtos = promtClient.getQuestion(promt);
            questionDtos.forEach(
                    questionService::createQuestion
            );

        } catch (Exception e) {
            log.error("Модель не доступна");
        }
    }
}
