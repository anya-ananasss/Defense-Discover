package backend.academy.notificationservice.service;

import backend.academy.notificationservice.dto.PasswordRepair;
import backend.academy.notificationservice.dto.UserConfirmation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserEventsMessageConsumerTest {

    @InjectMocks
    private UserEventsMessageConsumer consumer;

    @Mock
    private MessageService messageService;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldProcessUserConfirmationMessage() throws Exception {
        
        UserConfirmation confirmation = new UserConfirmation();
        confirmation.setEmail("test@test.com");
        confirmation.setCode("123456");
        String message = objectMapper.writeValueAsString(confirmation);

        
        consumer.processUserConfirmationMessage(message);

        
        verify(messageService).sendMail(any(UserConfirmation.class));
    }

    @Test
    void shouldProcessPasswordRepairMessage() throws Exception {
        
        PasswordRepair repair = new PasswordRepair();
        repair.setEmail("test@test.com");
        repair.setPassword("newPassword123");
        String message = objectMapper.writeValueAsString(repair);

        
        consumer.processPasswordRepairMessage(message);

        
        verify(messageService).sendMailRepairPassword(any(PasswordRepair.class));
    }

    @Test
    void shouldHandleInvalidMessage() {
        
        String invalidMessage = "invalid json";

        
        consumer.processUserConfirmationMessage(invalidMessage);

        
        verify(messageService, never()).sendMail(any(UserConfirmation.class));
    }
} 