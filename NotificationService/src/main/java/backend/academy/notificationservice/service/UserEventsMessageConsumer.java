package backend.academy.notificationservice.service;

import backend.academy.notificationservice.dto.UserConfirmation;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import static org.springframework.kafka.retrytopic.TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventsMessageConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MessageService messageService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${app.user-confirmation.topic}")
    private String topicDlt;

    @KafkaListener(
            containerFactory = "defaultConsumerFactory",
            topicPartitions =
                    @TopicPartition(
                            topic = "${app.user-confirmation.topic}",
                            partitions = {"0"}))
    @RetryableTopic(
            backoff = @Backoff(delay = 3000L, multiplier = 2.0),
            attempts = "2",
            autoCreateTopics = "false",
            kafkaTemplate = "userEventKafkaTemplate",
            topicSuffixingStrategy = SUFFIX_WITH_INDEX_VALUE,
            include = RuntimeException.class)
    @SuppressWarnings("FutureReturnValueIgnored")
    public void consume(ConsumerRecord<Long, String> record, Acknowledgment acknowledgment) {
        try {
            messageService.sendMail(objectMapper.readValue(record.value(), UserConfirmation.class));
        } catch (Exception e) {
            kafkaTemplate.send(topicDlt + "-dlt", record.toString());
        }

        acknowledgment.acknowledge();
    }
}
