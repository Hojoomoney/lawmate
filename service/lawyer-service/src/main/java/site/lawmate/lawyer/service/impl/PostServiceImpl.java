package site.lawmate.lawyer.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.lawmate.lawyer.domain.model.File;
import site.lawmate.lawyer.domain.model.Lawyer;
import site.lawmate.lawyer.domain.model.Post;
import site.lawmate.lawyer.repository.FileRepository;
import site.lawmate.lawyer.repository.LawyerRepository;
import site.lawmate.lawyer.repository.PostRepository;
import site.lawmate.lawyer.service.PostService;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final FileRepository fileRepository;
    private final LawyerRepository lawyerRepository;
    private final S3ServiceImpl s3ServiceImpl;
    private final AmazonS3 s3Client;
    private final String bucketName = "bucket-lawmate-lawyer";

    @Override
    public Mono<Lawyer> postToLawyer(String lawyerId, Post post, Flux<FilePart> fileParts) {
        return lawyerRepository.findById(lawyerId)
                .flatMap(lawyer -> {
                    post.setLawyerId(lawyer.getId());
                    return postRepository.save(post)
                            .flatMap(savedPost -> s3ServiceImpl.uploadFiles(fileParts)
                                    .collectList()
                                    .flatMap(urls -> {
                                        savedPost.setFileUrls(urls);
                                        return postRepository.save(savedPost);
                                    })
                                    .thenReturn(lawyer));
                });
    }
    @Override
    public Flux<File> saveFilesForPost(String lawyerId, Flux<FilePart> files, String postId) {
        return files.flatMap(file -> {
            String key = UUID.randomUUID().toString() + "_" + file.filename();
            return file.content().reduce(DataBuffer::write)
                    .flatMap(dataBuffer -> {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        DataBufferUtils.release(dataBuffer);

                        ObjectMetadata metadata = new ObjectMetadata();
                        metadata.setContentLength(bytes.length);
                        metadata.setContentType(file.headers().getContentType().toString());
                        s3Client.putObject(new PutObjectRequest(bucketName, key, new ByteArrayInputStream(bytes), metadata));
                        String url = s3Client.getUrl(bucketName, key).toString();

                        File fileModel = new File();
                        fileModel.setFilename(file.filename());
                        fileModel.setContentType(file.headers().getContentType().toString());
                        fileModel.setUrl(url);
                        fileModel.setLawyerId(lawyerId);
                        fileModel.setPostId(postId);
                        return fileRepository.save(fileModel);
                    });
        });
    }
    @Override
    public Flux<Post> getPostsByLawyerId(String lawyerId) {
        return postRepository.findAllByLawyerId(lawyerId);
    }
    @Override
    public Mono<Post> updatePost(String postId, Post updatedPost, Flux<FilePart> fileParts) {
        return postRepository.findById(postId)
                .flatMap(post -> {
                    post.setTitle(updatedPost.getTitle());
                    post.setContent(updatedPost.getContent());
                    post.setCategory(updatedPost.getCategory());
                    return postRepository.save(post)
                            .flatMap(updated -> saveFilesForPost(post.getLawyerId(), fileParts, postId)
                                    .map(File::getUrl)
                                    .collectList()
                                    .flatMap(urls -> {
                                        updated.setFileUrls(urls);
                                        return postRepository.save(updated);
                                    }));
                });
    }


    @Override
    public Flux<Post> getAllPosts(PageRequest pageRequest) {
        return postRepository.findAllBy(pageRequest);
    }
    @Override
    public Mono<Void> deletePost(String id) {
        return postRepository.findById(id)
                .flatMap(post -> fileRepository.findAllByPostId(id)
                        .flatMap(file -> deleteFileByUrl(file.getUrl())
                                .then(fileRepository.delete(file)))
                        .then(postRepository.deleteById(id)));
    }
    @Override
    public Mono<Void> deleteAllPosts() {
        return postRepository.findAll()
                .flatMap(post -> fileRepository.findAllByPostId(post.getId())
                        .flatMap(file -> deleteFileByUrl(file.getUrl())
                                .then(fileRepository.delete(file)))
                        .then(postRepository.delete(post)))
                .then();
    }
    @Override
    public Mono<Void> deleteFileByUrl(String url) {
        return Mono.fromRunnable(() -> {
            String key = url.substring(url.lastIndexOf("/") + 1);
            s3Client.deleteObject(bucketName, key);
        });
    }
    @Override
    public Mono<byte[]> downloadFile(String url) {
        return Mono.fromCallable(() -> {
            com.amazonaws.services.s3.model.S3Object s3Object = s3Client.getObject(bucketName, url.substring(url.lastIndexOf("/") + 1));
            return s3Object.getObjectContent().readAllBytes();
        });
    }

    @Override
    public Mono<Post> getPostById(String postId) {
        return postRepository.findById(postId);
    }
}