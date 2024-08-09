package site.lawmate.admin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.lawmate.admin.domain.model.ChatMessage;
import site.lawmate.admin.service.ChatService;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ReactiveKafkaProducerTemplate<String, ChatMessage> kafkaTemplate;
    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<ChatMessage> processMessage(ChatMessage chatMessage) {
        chatMessage.setTimestamp(LocalDateTime.now());
        return kafkaTemplate.send("advice", chatMessage)
                .then(mongoTemplate.save(chatMessage))
                .thenReturn(chatMessage);
    }
    @Override
    public Flux<ChatMessage> getChatMessages(String roomId) {
        Criteria criteria = Criteria.where("roomId").is(roomId);
        Query query = new Query(criteria);
        log.info("Query: {}", query);
        return mongoTemplate.find(query, ChatMessage.class);
    }
    @Override
    public Mono<ChatMessage> createChatRoom(String sender, String receiver) {
        ChatMessage chatMessage = ChatMessage.builder()
                .roomId(UUID.randomUUID().toString())
                .sender(sender)
                .receiver(receiver)
                .message("채팅방이 생성되었습니다. 대화를 시작해보세요!")
                .build();
        return mongoTemplate.save(chatMessage);
    }

    @Override
    public Mono<String> getRoomId(String sender, String receiver) {

        Criteria criteria = new Criteria().orOperator(
                Criteria.where("sender").is(sender).and("receiver").is(receiver),
                Criteria.where("sender").is(receiver).and("receiver").is(sender)
        );
        Query query = new Query(criteria);
        return mongoTemplate.findOne(query, ChatMessage.class)
                .map(ChatMessage::getRoomId);

    }
}
