package site.lawmate.lawyer.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.lawmate.lawyer.domain.model.File;
import site.lawmate.lawyer.service.impl.FileServiceImpl;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
        @ApiResponse(responseCode = "404", description = "Customer not found")})
@RequestMapping("/files")
public class FileController {

    private final FileServiceImpl fileService;

    @PostMapping("/upload/{lawyerId}")
    public ResponseEntity<Flux<File>> uploadFile(@PathVariable("lawyerId") String lawyerId,
                                                 @RequestPart("files") Flux<FilePart> files) {
        return ResponseEntity.ok(fileService.saveFiles(lawyerId, files));
    }

    @GetMapping("/download/{id}")
    public Mono<ResponseEntity<ByteArrayResource>> downloadFile(@PathVariable("id") String id) {
        return fileService.getFileById(id)
                .flatMap(file -> fileService.downloadFile(file.getUrl())
                        .map(byteArray -> ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                                .contentType(MediaType.parseMediaType(file.getContentType()))
                                .body(new ByteArrayResource(byteArray))))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{lawyerId}")
    public ResponseEntity<Flux<File>> getFilesByLawyerId(@PathVariable("lawyerId") String lawyerId) {
        return ResponseEntity.ok(fileService.getFilesByLawyerId(lawyerId));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteFile(@PathVariable("id") String id) {
        return fileService.getFileById(id)
                .flatMap(file -> fileService.deleteFileByUrl(file.getUrl())
                        .then(fileService.deleteFileById(id))
                        .thenReturn(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete")
    public Mono<ResponseEntity<Void>> deleteAllFiles() {
        return fileService.getAllFiles()
                .flatMap(file -> fileService.deleteFileByUrl(file.getUrl())
                        .then(fileService.deleteFileById(file.getId())))
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }
}