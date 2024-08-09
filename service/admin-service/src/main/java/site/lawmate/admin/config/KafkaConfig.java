package site.lawmate.admin.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.SenderOptions;
import site.lawmate.admin.domain.model.ChatMessage;

@Configuration
public class KafkaConfig {

    @Bean
    public ReactiveKafkaProducerTemplate<String, ChatMessage> reactiveKafkaProducerTemplate(KafkaProperties kafkaProperties) {
        SenderOptions<String, ChatMessage> senderOptions = SenderOptions.create(kafkaProperties.buildProducerProperties(null));
        return new ReactiveKafkaProducerTemplate<>(senderOptions);
    }

    @Bean
    public ReactiveKafkaConsumerTemplate<String, ChatMessage> reactiveKafkaConsumerTemplate(KafkaProperties kafkaProperties) {
        ReceiverOptions<String, ChatMessage> receiverOptions = ReceiverOptions.create(kafkaProperties.buildProducerProperties(null));
        return new ReactiveKafkaConsumerTemplate<>(receiverOptions);
    }

    @Bean
    public NewTopic adviceTopic() {
        return TopicBuilder.name("advice")
                .partitions(10)
                .replicas(1)
                .build();
    }
}
