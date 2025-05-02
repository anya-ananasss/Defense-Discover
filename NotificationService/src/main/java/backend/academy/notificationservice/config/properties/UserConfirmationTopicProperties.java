package backend.academy.notificationservice.config.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Configuration
@ConfigurationProperties("app.user-confirmation")
public class UserConfirmationTopicProperties {

    @Value("${app.user-confirmation.topic}")
    private String topic;

    @Value("${app.user-confirmation.concurrency}")
    private int concurrency;
}
