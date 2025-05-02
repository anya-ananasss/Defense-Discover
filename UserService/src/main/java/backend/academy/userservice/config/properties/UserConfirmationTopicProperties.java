package backend.academy.userservice.config.properties;

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
@ConfigurationProperties("app.stat")
public class UserConfirmationTopicProperties {

    @Value("${app.stat.topic}")
    private String topic;

    @Value("${app.stat.concurrency}")
    private int concurrency;
}
