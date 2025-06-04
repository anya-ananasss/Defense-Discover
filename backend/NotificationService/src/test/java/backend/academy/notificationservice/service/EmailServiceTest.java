package backend.academy.notificationservice.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {EmailService.class})
@TestPropertySource(properties = {
    "spring.mail.host=localhost",
    "spring.mail.port=25",
    "app.mail=test@example.com"
})
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @MockBean
    private JavaMailSender mailSender;

    @Test
    void shouldSendEmail() {
        String to = "test@test.com";
        String subject = "Test Subject";
        String text = "Test Message";


        emailService.sendSimpleMessage(to, subject, text);

        verify(mailSender).send(any(SimpleMailMessage.class));
    }
} 