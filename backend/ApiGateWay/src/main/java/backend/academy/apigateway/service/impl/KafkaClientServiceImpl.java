package backend.academy.apigateway.service.impl;


import backend.academy.apigateway.dto.PasswordRepair;
import backend.academy.apigateway.dto.UserConfirmation;
import backend.academy.apigateway.service.KafkaClientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaClientServiceImpl implements KafkaClientService {

    private final KafkaTemplate<Long, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.user-confirmation.topic}")
    private String topic;

    @Value("${app.password-repair.topic}")
    private String topicPasswordRepair;

    public void sendNotificationStackOverflow(UserConfirmation userConfirmation)  {
        try {
            System.out.println("сообщение отправлено");
            kafkaTemplate.send(topic, objectMapper.writeValueAsString(userConfirmation));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void sendPasswordRepair(PasswordRepair passwordRepair)  {
        try {
            System.out.println("сообщение отправлено");
            kafkaTemplate.send(topicPasswordRepair, objectMapper.writeValueAsString(passwordRepair));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
