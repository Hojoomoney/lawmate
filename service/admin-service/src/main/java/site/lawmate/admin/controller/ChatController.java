package site.lawmate.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.lawmate.admin.handler.ChatListener;
import site.lawmate.admin.domain.model.ChatMessage;
import site.lawmate.admin.service.impl.ChatServiceImpl;

import java.time.Duration;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatServiceImpl chatServiceImpl;
    private final ChatListener chatListener;
    @GetMapping(value = "/messages", produces = "text/event-stream")
    public Flux<ServerSentEvent<ChatMessage>> getMessages(@RequestParam String roomId) {
        log.info("roomId: {}", roomId);
        Flux<ServerSentEvent<ChatMessage>> messageStream = chatListener.getChatMessages(roomId)
                .map(message -> {
                    log.info("Sending message: {}", message);
                    return ServerSentEvent.builder(message).build();
                });

        // Keep-Alive 메시지 스트림
        Flux<ServerSentEvent<ChatMessage>> keepAliveStream = Flux.interval(Duration.ofSeconds(10))
                .map(seq -> {
                    log.debug("Sending keep-alive message");
                    return ServerSentEvent.<ChatMessage>builder()
                            .comment("keep-alive")
                            .build();
                });

        return Flux.merge(messageStream, keepAliveStream)
                .doOnCancel(() -> log.info("SSE connection closed for roomId: {}", roomId));
    }

    @PostMapping("/send")
    public Mono<Void> sendMessage(@RequestBody ChatMessage chatMessage) {
        return chatServiceImpl.processMessage(chatMessage).then();
    }

    @GetMapping("/create")
    public Mono<ChatMessage> createChatRoom(@RequestParam("sender") String sender, @RequestParam("receiver") String receiver) {
        return chatServiceImpl.createChatRoom(sender, receiver);
    }

    @GetMapping("/history")
    public Flux<ChatMessage> getChatHistory(@RequestParam String roomId) {
        return chatServiceImpl.getChatMessages(roomId);
    }

    @GetMapping("/room")
    public Mono<String> getRoomId(@RequestParam String sender, @RequestParam String receiver) {
        return chatServiceImpl.getRoomId(sender, receiver);
    }
}
