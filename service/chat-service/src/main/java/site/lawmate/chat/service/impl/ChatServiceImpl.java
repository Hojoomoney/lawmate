package site.lawmate.chat.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.lawmate.chat.domain.model.Chat;
import site.lawmate.chat.domain.dto.ChatDto;
import site.lawmate.chat.repository.ChatRepository;
import site.lawmate.chat.service.ChatService;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    @Override
    public Mono<Chat> save(ChatDto chatDto) {
        // userId와 chattingRoomId로 최대 messageOrder 조회
        Mono<Integer> maxOrderMono = chatRepository.findByUserIdAndChattingRoomId(chatDto.getUserId(), chatDto.getChattingRoomId())
                .sort(Comparator.comparing(Chat::getMessageOrder).reversed())
                .next()
                .map(Chat::getMessageOrder)
                .switchIfEmpty(Mono.just(0));

        // 최대 messageOrder가 없는 경우 1로 초기화
        return maxOrderMono.flatMap(maxOrder -> {
            // 조회된 최대 messageOrder에 1을 더하여 chatDto에 설정
            chatDto.setMessageOrder(maxOrder + 1);
            return chatRepository.save(Chat.builder()
                    .userId(chatDto.getUserId())
                    .chattingRoomId(chatDto.getChattingRoomId())
                    .question(chatDto.getQuestion())
                    .answer(chatDto.getAnswer())
                    .messageOrder(chatDto.getMessageOrder())
                    .chatDate(LocalDateTime.now())
                    .build());
                });
    }

    @Override
    public Mono<Void> deleteByChatDto(ChatDto chatDto) {
        return chatRepository.deleteByUserIdAndChattingRoomId(chatDto.getUserId(), chatDto.getChattingRoomId());
    }


    //    @Override 코드 전체 수정할 때 활용 여부 검토
//    public Flux<ChatDto> getChatListByUserId(Long userId) {
//        return chatRepository.findAllByUserId(userId)
//                .collectList()
//                .flatMapMany(chats -> {
//                    Map<Long, Chat> chatMap = new HashMap<>();
//                    for (Chat chat : chats) {
//                        chatMap.merge(chat.getChattingRoomId(), chat, (existing, newChat) -> {
//                            if (existing.getMessageOrder() > newChat.getMessageOrder()) {
//                                return existing;
//                            } else {
//                                return newChat;
//                            }
//                        });
//                    }
//                    return Flux.fromIterable(chatMap.values())
//                            .map(chat -> ChatDto.builder()
//                                    .id(chat.getId())
//                                    .userId(chat.getUserId())
//                                    .chattingRoomId(chat.getChattingRoomId())
//                                    .question(chat.getQuestion())
//                                    .answer(chat.getAnswer())
//                                    .messageOrder(chat.getMessageOrder())
//                                    .chatDate(chat.getChatDate())
//                                    .chatName(chat.getQuestion())
//                                    .recentChat(chat.getChatDate())
//                                    .build());
//                });
//    }

    @Override
    public Flux<ChatDto> getChatListByUserId(Long userId) {
        return chatRepository.findAllByUserId(userId)
                .collectMultimap(Chat::getChattingRoomId) // chattingRoomId로 그룹화
                .flatMapMany(chatRoomMap -> {
                    List<Chat> latestChats = new ArrayList<>();
                    chatRoomMap.forEach((roomId, roomChats) -> {
                        Chat oldestChat = roomChats.stream()
                                .min(Comparator.comparing(Chat::getMessageOrder))
                                .orElseThrow(NoSuchElementException::new);
                        Chat latestChat = roomChats.stream()
                                .max(Comparator.comparing(Chat::getMessageOrder))
                                .orElseThrow(NoSuchElementException::new);
                        latestChats.add(latestChat);
                    });
                    return Flux.fromIterable(latestChats)
                            .map(chat -> {
                                // 옛날 채팅 찾기.
                                Chat oldestChat = chatRoomMap.get(chat.getChattingRoomId())
                                        .stream()
                                        .min(Comparator.comparing(Chat::getMessageOrder))
                                        .orElseThrow(NoSuchElementException::new);

                                return ChatDto.builder()
                                        .chattingRoomId(chat.getChattingRoomId())
                                        .chatName(oldestChat.getQuestion()) // 옛날 채팅 question -> chatName
                                        .recentChat(chat.getChatDate()) // 최근 채팅 recentChat
                                        .oldestChatDate(oldestChat.getChatDate()) // 옛날 채팅 날짜 -> oldestChatDate
                                        .build();
                            });
                });
    }

    @Override
    public Mono<Long> getRoomId(Long userId) {
        return chatRepository.findByUserId(userId)
                .sort(Comparator.comparing(Chat::getChattingRoomId).reversed())
                .next()
                .map(chat -> chat.getChattingRoomId() + 1)
                .switchIfEmpty(Mono.just(1L));
    }

    @Override
    public Flux<ChatDto> getChatHistoryByMessageOrder(Long userId, Long chattingRoomId) {
        return chatRepository.findByUserIdAndChattingRoomIdOrderByMessageOrderAsc(userId, chattingRoomId)
                .map(this::convertToDto);
    }

    @Override
    public Flux<ChatDto> getChatHistoryByChatDate(Long userId, Long chattingRoomId) {
        return chatRepository.findByUserIdAndChattingRoomIdOrderByChatDateAsc(userId, chattingRoomId)
                .map(this::convertToDto);
    }



    private ChatDto convertToDto(Chat chat) {
        return ChatDto.builder()
                .id(chat.getId())
                .userId(chat.getUserId())
                .chattingRoomId(chat.getChattingRoomId())
                .question(chat.getQuestion())
                .answer(chat.getAnswer())
                .messageOrder(chat.getMessageOrder())
                .chatDate(chat.getChatDate())
                .build();
    }


}

