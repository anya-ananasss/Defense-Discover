package backend.academy.notificationservice.service;

import backend.academy.notificationservice.dto.PasswordRepair;
import backend.academy.notificationservice.dto.UserConfirmation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserEventsMessageConsumerTest {

    @InjectMocks
    private UserEventsMessageConsumer consumer;

    @Mock
    private MessageService messageService;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private Acknowledgment acknowledgment;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldProcessUserConfirmationMessage() throws Exception {
        UserConfirmation confirmation = new UserConfirmation();
        confirmation.setEmail("test@test.com");
        confirmation.setCode("123456");
        String message = objectMapper.writeValueAsString(confirmation);
        ConsumerRecord<Long, String> record = new ConsumerRecord<>("topic", 0, 0L, 1L, message);


        consumer.consume(record, acknowledgment);


        verify(messageService).sendMail(any(UserConfirmation.class));
        verify(acknowledgment).acknowledge();
        verify(kafkaTemplate, never()).send(anyString(), anyString());
    }

    @Test
    void shouldProcessPasswordRepairMessage() throws Exception {
        PasswordRepair repair = new PasswordRepair();
        repair.setEmail("test@test.com");
        repair.setPassword("newPassword123");
        String message = objectMapper.writeValueAsString(repair);
        ConsumerRecord<Long, String> record = new ConsumerRecord<>("topic", 0, 0L, 1L, message);


        consumer.consumeRepairPassword(record, acknowledgment);


        verify(messageService).sendMailRepairPassword(any(PasswordRepair.class));
        verify(acknowledgment).acknowledge();
        verify(kafkaTemplate, never()).send(anyString(), anyString());
    }

    @Test
    void shouldHandleInvalidMessage() {
        String invalidMessage = "invalid json";
        ConsumerRecord<Long, String> record = new ConsumerRecord<>("topic", 0, 0L, 1L, invalidMessage);
        when(kafkaTemplate.send(anyString(), anyString())).thenReturn(null);

        consumer.consume(record, acknowledgment);

        verify(messageService, never()).sendMail(any(UserConfirmation.class));
        verify(acknowledgment).acknowledge();
        verify(kafkaTemplate).send(endsWith("-dlt"), anyString());
    }
}