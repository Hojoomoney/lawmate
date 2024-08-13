package site.lawmate.lawyer.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import site.lawmate.lawyer.service.impl.NoticeServiceImpl;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
        @ApiResponse(responseCode = "404", description = "Customer not found")})
@RequestMapping(path = "/notifications")
public class NoticeController {

    private final NoticeServiceImpl noticeService;

    @GetMapping(value = "/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamNotifications(@PathVariable String userId) {
        return noticeService.getNotifications(userId);
    }

    @PostMapping("/accept")
    public void acceptNotification(@RequestParam String userId) {
        String message = "상담 요청이 수락되었습니다.";
        noticeService.sendNotification(userId,  message);
    }

    @PostMapping("/reject")
    public void rejectNotification(@RequestParam String userId) {
        String message = "상담 요청이 거절되었습니다.";
        noticeService.sendNotification(userId, message);
    }

    @DeleteMapping("/{userId}")
    public void removeUserSink(@PathVariable String userId) {
        noticeService.removeUserSink(userId);
    }
}
