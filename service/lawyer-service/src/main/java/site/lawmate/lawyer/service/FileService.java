package site.lawmate.lawyer.service;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.lawmate.lawyer.domain.model.File;

public interface FileService {

    Flux<File> saveFiles(String lawyerId, Flux<FilePart> files);
    Mono<String> uploadToS3(FilePart filePart);
    Mono<File> saveFileMetadata(String lawyerId, FilePart filePart, String url);
    Mono<File> getFileById(String id);
    Mono<Void> deleteFileById(String id);
    Mono<byte[]> downloadFile(String url);
    Mono<Void> deleteFileByUrl(String url);
    Mono<Void> deleteAllFiles();
    Flux<File> getAllFiles();
    Flux<File> getFilesByLawyerId(String lawyerId);
    String getKeyFromUrl(String url);


}
