package backend.academy.notificationservice.config;

import backend.academy.notificationservice.dto.UserConfirmation;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.RoundRobinAssignor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;
import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final KafkaProperties properties;

    @Bean("defaultConsumerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Long, UserConfirmation>>
            defaultConsumerFactory() {
        ConcurrentKafkaListenerContainerFactory<Long, UserConfirmation> factory =
                new ConcurrentKafkaListenerContainerFactory<Long, UserConfirmation>();
        factory.setConsumerFactory(
                consumerFactory(props -> props.put(ConsumerConfig.GROUP_ID_CONFIG, "default-consumer")));

        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.setAutoStartup(true);
        factory.setConcurrency(1);
        return factory;
    }

    @Bean
    public KafkaTemplate<Long, UserConfirmation> userEventKafkaTemplate() {
        Map<String, Object> props = properties.buildProducerProperties(null);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, UserEventSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "0");
        var factory = new DefaultKafkaProducerFactory<Long, UserConfirmation>(props);
        return new KafkaTemplate<>(factory);
    }

    private <M> ConsumerFactory<Long, M> consumerFactory(Consumer<Map<String, Object>> propsModifier) {
        var props = properties.buildConsumerProperties(null);

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, RoundRobinAssignor.class.getName());

        propsModifier.accept(props);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    public static class UserEventSerializer extends JsonSerializer<UserConfirmation> {}
}
