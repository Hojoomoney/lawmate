package site.lawmate.lawyer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.lawmate.lawyer.domain.model.Lawyer;
import site.lawmate.lawyer.domain.model.Post;
import site.lawmate.lawyer.service.impl.PostServiceImpl;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
        @ApiResponse(responseCode = "404", description = "Customer not found")})
@RequestMapping(path = "/posts")
public class PostController {
    private final PostServiceImpl service;

    @PostMapping(value = "/save/{lawyerId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Mono<Lawyer>> createPost(@PathVariable("lawyerId") String lawyerId,
                                                   @RequestPart("post") String postJson,
                                                   @RequestPart(value = "files", required = false) Flux<FilePart> fileParts) throws JsonProcessingException {
        Post post = new ObjectMapper().readValue(postJson, Post.class); // JSON 문자열을 Post 객체로 변환
        if (fileParts == null) {
            fileParts = Flux.empty(); // 파일이 없으면 빈 Flux로 처리
        }
        return ResponseEntity.ok(service.postToLawyer(lawyerId, post, fileParts));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flux<Post>> getPostsByLawyerId(@PathVariable("id") String id) {
        return ResponseEntity.ok(service.getPostsByLawyerId(id));
    }

    @PatchMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Mono<Post>> updatePost(
            @PathVariable("postId") String postId,
            @RequestPart("post") Post updatedPost,
            @RequestPart("files") Flux<FilePart> fileParts) {
        return ResponseEntity.ok(service.updatePost(postId, updatedPost, fileParts));
    }

    @GetMapping("/all")
    public ResponseEntity<Flux<Post>> getAllPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(service.getAllPosts(PageRequest.of(page, size)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Mono<Void>> deletePost(@PathVariable("id") String id) {
        return ResponseEntity.ok(service.deletePost(id));
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<Mono<Void>> deleteAllPosts() {
        return ResponseEntity.ok(service.deleteAllPosts());
    }

    @GetMapping("/download")
    public Mono<ResponseEntity<ByteArrayResource>> downloadFile(@RequestParam String url) {
        return service.downloadFile(url)
                .map(bytes -> {
                    ByteArrayResource resource = new ByteArrayResource(bytes);
                    return ResponseEntity.ok()
                            .contentLength(bytes.length)
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + url.substring(url.lastIndexOf("/") + 1))
                            .body(resource);
                });
    }

    @GetMapping("/find/{postId}")
    public ResponseEntity<Mono<Post>> getPostById(@PathVariable("postId") String postId) {
        return ResponseEntity.ok(service.getPostById(postId));
    }
}