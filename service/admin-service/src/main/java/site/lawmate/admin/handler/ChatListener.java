package site.lawmate.admin.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import site.lawmate.admin.domain.model.ChatMessage;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatListener {

    private final Sinks.Many<ChatMessage> chatSink;

    @KafkaListener(topics = "advice")
    public void listen(ChatMessage chatMessage) {
        log.info("Received message: {}", chatMessage);
        chatSink.tryEmitNext(chatMessage);
    }

    public Flux<ChatMessage> getChatMessages(String roomId) {
        log.info("Subscribing to messages for roomId: {}", roomId);
        return chatSink.asFlux()
                .doOnNext(chatMessage -> log.info("Received from sink: {}", chatMessage))
                .filter(chatMessage -> chatMessage.getRoomId().equals(roomId))
                .doOnNext(chatMessage -> log.info("Filtered message for room {}: {}", roomId, chatMessage));
    }

}

