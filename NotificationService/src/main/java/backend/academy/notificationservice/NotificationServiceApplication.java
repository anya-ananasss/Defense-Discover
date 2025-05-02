package backend.academy.notificationservice;

import backend.academy.notificationservice.config.properties.UserConfirmationTopicProperties;
import backend.academy.notificationservice.dto.UserConfirmation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({UserConfirmationTopicProperties.class})
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

}
