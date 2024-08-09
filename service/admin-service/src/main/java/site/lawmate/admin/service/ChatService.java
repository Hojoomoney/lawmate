package site.lawmate.admin.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.lawmate.admin.domain.model.ChatMessage;

public interface ChatService {
    Mono<ChatMessage> processMessage(ChatMessage chatMessage);
    Flux<ChatMessage> getChatMessages(String roomId);
    Mono<ChatMessage> createChatRoom(String sender, String receiver);

    Mono<String> getRoomId(String sender, String receiver);
}
