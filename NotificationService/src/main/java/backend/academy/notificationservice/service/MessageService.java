package backend.academy.notificationservice.service;

import backend.academy.notificationservice.dto.UserConfirmation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final EmailService emailService;

    public void sendMail(UserConfirmation userConfirmation) {
        System.out.println(userConfirmation);

        emailService.sendSimpleMessage(
                userConfirmation.getEmail(),
                "Подтвердите свой аккаунт на Defense-Discover",
                "Ваш код подтверждения: " + userConfirmation.getCode()
        );

    }
}
