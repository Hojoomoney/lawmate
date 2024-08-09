package site.lawmate.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.lawmate.chat.domain.model.Chat;
import site.lawmate.chat.domain.dto.ChatDto;
import site.lawmate.chat.service.ChatService;

@Slf4j
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    //커맨드 (create, update, delete)
    @PostMapping("/temp")
    @ResponseStatus(HttpStatus.OK)
    public Mono<String> temp(@RequestBody Mono<String> tempQuestion){
        return tempQuestion.doOnNext(question -> log.info("질문받은 메세지 : {}", question))
                .map(answer -> "안녕하세요?");
    }

    //채팅: 응답 후 저장 (성능 고려)
    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Chat> save(@RequestBody Mono<ChatDto> chatDtoMono){
        return chatDtoMono.doOnNext(chatDto -> log.info("컨트롤러단 save : {}", chatDto.getAnswer()))
                .flatMap(chatService::save);
    }

    //채팅 삭제
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@RequestBody ChatDto chatRequestDto){
        log.info("컨트롤러단 dto : {}", chatRequestDto);
        return chatService.deleteByChatDto(chatRequestDto);
    }

    //쿼리 (read)
    //채팅 리스트
    @GetMapping("/list/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<ChatDto> getChatList(@PathVariable Long userId) {
        return chatService.getChatListByUserId(userId);
    }

    //새 채팅을 요구한 경우 (새로운 RoomId를 프론트로 보냄)
    @GetMapping("/newRoomId/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Long> getRoomId(@PathVariable Long userId) {
        return chatService.getRoomId(userId);
    }

    //기존 채팅 조회 (message-order 걷어낼 예정)
    @PostMapping("/history/message-order")
    @ResponseStatus(HttpStatus.OK)
    public Flux<ChatDto> getChatHistoryByMessageOrder(@RequestBody ChatDto chatRequestDto) {
        return chatService.getChatHistoryByMessageOrder(chatRequestDto.getUserId(), chatRequestDto.getChattingRoomId());
    }

    //기존 채팅 조회
    @PostMapping("/history/chat-date")
    @ResponseStatus(HttpStatus.OK)
    public Flux<ChatDto> getChatHistoryByChatDate(@RequestBody ChatDto chatRequestDto) {
        return chatService.getChatHistoryByChatDate(chatRequestDto.getUserId(), chatRequestDto.getChattingRoomId());
    }





}
