package site.lawmate.lawyer.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.lawmate.lawyer.domain.model.Lawyer;
import site.lawmate.lawyer.domain.model.Reply;
import site.lawmate.lawyer.service.impl.ReplyServiceImpl;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
        @ApiResponse(responseCode = "404", description = "Customer not found")})
@RequestMapping(path = "/replies")
public class ReplyController {
    private final ReplyServiceImpl service;

    @GetMapping("/article/{articleId}")
    public ResponseEntity<Mono<Reply>> findReplyByArticleId(@PathVariable("articleId") String articleId) {
        return ResponseEntity.ok(service.getReplyByArticleId(articleId));
    }

    @PostMapping("/save/{id}")
    public ResponseEntity<Mono<Lawyer>> createReply(@PathVariable("id") String lawyerId,
                                                         @RequestParam String articleId,
                                                         @RequestBody Reply reply) {
        return ResponseEntity.ok(service.replyToLawyer(lawyerId, articleId, reply));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flux<Reply>> getRepliesByLawyerId(@PathVariable("id") String id) {
        return ResponseEntity.ok(service.getRepliesByLawyerId(id));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Mono<Reply>> updateReply(@PathVariable("id") String id, @RequestBody Reply reply) {
        return ResponseEntity.ok(service.updateReply(id, reply));
    }

    @GetMapping("/all")
    public ResponseEntity<Flux<Reply>> getAllReplies() {
        return ResponseEntity.ok(service.getAllReplies());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Mono<Void>> deleteReply(@PathVariable("id") String id) {
        return ResponseEntity.ok(service.deleteReply(id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Mono<Void>> deleteAllReplies() {
        return ResponseEntity.ok(service.deleteAllReplies());
    }
}
