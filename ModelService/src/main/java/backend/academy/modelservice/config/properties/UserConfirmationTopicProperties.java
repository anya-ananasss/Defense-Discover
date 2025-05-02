package backend.academy.modelservice.config.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties("app.user-confirmation")
public class UserConfirmationTopicProperties {

    @Value("${app.stat.topic}")
    private String topic;

    @Value("${app.stat.partitions}")
    private String partitions;

    @Value("${app.stat.replicas}")
    private String replicas;

    public KafkaAdmin.NewTopics toNewTopics() {
        return new KafkaAdmin.NewTopics(
                new NewTopic(topic, Integer.parseInt(partitions), Short.parseShort(replicas)),
                new NewTopic(topic + "-dlt", Integer.parseInt(partitions), Short.parseShort(replicas)));
    }
}
