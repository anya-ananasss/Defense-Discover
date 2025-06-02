package backend.academy.modelservice;

import backend.academy.modelservice.client.PromtClient;
import backend.academy.modelservice.service.KafkaClientService;
import backend.academy.modelservice.service.PromtService;
import backend.academy.modelservice.service.QuestionService;
import backend.academy.modelservice.service.RedisService;
import backend.academy.modelservice.service.impl.PromtServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    @Primary
    public PromtClient promtClient() {
        return Mockito.mock(PromtClient.class);
    }

    @Bean
    @Primary
    public RedisService redisService() {
        return Mockito.mock(RedisService.class);
    }

    @Bean
    @Primary
    public QuestionService questionService() {
        return Mockito.mock(QuestionService.class);
    }

    @Bean
    @Primary
    public KafkaClientService kafkaClientService() {
        return Mockito.mock(KafkaClientService.class);
    }

    @Bean
    @Primary
    public PromtService promtService(
        RedisService redisService,
        QuestionService questionService,
        ObjectMapper objectMapper,
        PromtClient promtClient,
        KafkaClientService kafkaClientService
    ) {
        return new PromtServiceImpl(redisService, questionService, objectMapper, promtClient, kafkaClientService);
    }
}