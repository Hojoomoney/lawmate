package site.lawmate.chat.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.lawmate.chat.domain.model.Chat;
import site.lawmate.chat.domain.dto.ChatDto;

public interface ChatService {

    Mono<Chat> save(ChatDto chatDto);

    Mono<Void> deleteByChatDto(ChatDto chatDto);

    Flux<ChatDto> getChatListByUserId(Long userId);

    Mono<Long> getRoomId(Long userId);

    Flux<ChatDto> getChatHistoryByMessageOrder(Long userId, Long chattingRoomId);

    Flux<ChatDto> getChatHistoryByChatDate(Long userId, Long chattingRoomId);

}
