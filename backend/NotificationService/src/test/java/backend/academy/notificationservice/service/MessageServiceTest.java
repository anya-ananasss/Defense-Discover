package backend.academy.notificationservice.service;

import backend.academy.notificationservice.dto.PasswordRepair;
import backend.academy.notificationservice.dto.UserConfirmation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {MessageService.class, EmailService.class})
@TestPropertySource(properties = {
    "spring.mail.host=localhost",
    "spring.mail.port=25",
    "app.mail=test@example.com"
})
class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @MockBean
    private EmailService emailService;

    @Test
    void shouldSendConfirmationEmail() {
        
        UserConfirmation confirmation = new UserConfirmation();
        confirmation.setEmail("test@test.com");
        confirmation.setCode("123456");

        
        messageService.sendMail(confirmation);

        
        verify(emailService).sendSimpleMessage(
            eq("test@test.com"),
            eq("Подтвердите свой аккаунт на Defense-Discover"),
            eq("Ваш код подтверждения: 123456")
        );
    }

    @Test
    void shouldSendPasswordRepairEmail() {
        
        PasswordRepair repair = new PasswordRepair();
        repair.setEmail("test@test.com");
        repair.setPassword("newPassword123");

        
        messageService.sendMailRepairPassword(repair);

        
        verify(emailService).sendSimpleMessage(
            eq("test@test.com"),
            eq("Восстановление пароля на Defense-Discover"),
            eq("Ваш новый пароль: newPassword123")
        );
    }
} 