package site.lawmate.lawyer.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import site.lawmate.lawyer.domain.model.File;
import site.lawmate.lawyer.repository.FileRepository;
import site.lawmate.lawyer.repository.LawyerRepository;
import site.lawmate.lawyer.service.FileService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final LawyerRepository lawyerRepository;
    private final AmazonS3 s3Client;
    private final S3ServiceImpl s3ServiceImpl;
    private final String bucketName = "bucket-lawmate-lawyer";

    @Override
    public Flux<File> saveFiles(String lawyerId, Flux<FilePart> files) {
        return lawyerRepository.findById(lawyerId)
                .flatMapMany(lawyer -> files.flatMap(filePart -> s3ServiceImpl.uploadFile(filePart)
                        .flatMap(url -> saveFileMetadata(lawyerId, filePart, url))));
    }

    @Override
    public Mono<File> saveFileMetadata(String lawyerId, FilePart filePart, String url) {
        File fileModel = new File();
        fileModel.setFilename(filePart.filename());
        fileModel.setContentType(filePart.headers().getContentType().toString());
        fileModel.setUrl(url);
        fileModel.setLawyerId(lawyerId);
        return fileRepository.save(fileModel);
    }

    @Override
    public Mono<File> getFileById(String id) {
        return fileRepository.findById(id);
    }

    @Override
    public Mono<Void> deleteFileById(String id) {
        return fileRepository.deleteById(id);
    }

    @Override
    public Mono<Void> deleteAllFiles() {
        return fileRepository.deleteAll();
    }

    @Override
    public Mono<byte[]> downloadFile(String url) {
        return Mono.fromCallable(() -> {
            String key = getKeyFromUrl(url);
            com.amazonaws.services.s3.model.S3Object s3Object = s3Client.getObject(bucketName, key);
            try (InputStream inputStream = s3Object.getObjectContent()) {
                return inputStream.readAllBytes();
            }
        });
    }

    @Override
    public Mono<Void> deleteFileByUrl(String url) {
        return Mono.fromRunnable(() -> s3Client.deleteObject(bucketName, getKeyFromUrl(url)));
    }

    @Override
    public Flux<File> getAllFiles() {
        return fileRepository.findAll();
    }

    @Override
    public Flux<File> getFilesByLawyerId(String lawyerId) {
        return fileRepository.findAllByLawyerId(lawyerId);
    }
    @Override
    public String getKeyFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }
}
