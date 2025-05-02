package backend.academy.modelservice.service.impl;


import backend.academy.modelservice.dto.StatEvent;
import backend.academy.modelservice.service.KafkaClientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaClientServiceImpl implements KafkaClientService {

    private final KafkaTemplate<Long, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.stat.topic}")
    private String topic;

    public void sendNotificationStackOverflow(StatEvent statEvent) throws JsonProcessingException {
        System.out.println("сообщение отправлено");
        kafkaTemplate.send(topic, objectMapper.writeValueAsString(statEvent));
    }
}
