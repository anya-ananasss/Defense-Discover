package backend.academy.apigateway.service.impl;


import backend.academy.apigateway.dto.UserConfirmation;
import backend.academy.apigateway.service.KafkaClientService;
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

    @Value("${app.user-confirmation.topic}")
    private String topic;

    public void sendNotificationStackOverflow(UserConfirmation userConfirmation) throws JsonProcessingException {
        System.out.println("сообщение отправлено");
        kafkaTemplate.send(topic, objectMapper.writeValueAsString(userConfirmation));
    }
}
