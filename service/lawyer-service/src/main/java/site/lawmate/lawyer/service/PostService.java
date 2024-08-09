package site.lawmate.lawyer.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.lawmate.lawyer.domain.model.File;
import site.lawmate.lawyer.domain.model.Lawyer;
import site.lawmate.lawyer.domain.model.Post;

public interface PostService {
    Mono<Lawyer> postToLawyer(String lawyerId, Post post, Flux<FilePart> fileParts);
    Flux<File> saveFilesForPost(String lawyerId, Flux<FilePart> files, String postId);
    Flux<Post> getPostsByLawyerId(String lawyerId);
    Mono<Post> updatePost(String postId, Post updatedPost, Flux<FilePart> fileParts);
    Flux<Post> getAllPosts(PageRequest pageRequest);
    Mono<Void> deletePost(String id);
    Mono<Void> deleteAllPosts();
    Mono<Void> deleteFileByUrl(String url);
    Mono<byte[]> downloadFile(String url);


}
