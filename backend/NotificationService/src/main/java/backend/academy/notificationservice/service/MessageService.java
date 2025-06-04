package backend.academy.notificationservice.service;

import backend.academy.notificationservice.dto.PasswordRepair;
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

    public void sendMailRepairPassword(PasswordRepair passwordRepair) {
        System.out.println(passwordRepair);

        emailService.sendSimpleMessage(
                passwordRepair.getEmail(),
                "Восстановление пароля на Defense-Discover",
                "Ваш новый пароль: " + passwordRepair.getPassword()
        );

    }
}
