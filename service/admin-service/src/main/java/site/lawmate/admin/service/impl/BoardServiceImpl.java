package site.lawmate.admin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.lawmate.admin.domain.dto.BoardDto;
import site.lawmate.admin.domain.model.File;
import site.lawmate.admin.domain.model.Board;
import site.lawmate.admin.repository.BoardRepository;
import site.lawmate.admin.service.BoardService;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Override
    public Flux<BoardDto> findAll(PageRequest pageRequest) {
        return boardRepository.findAllBy(pageRequest)
                .flatMap(board -> Flux.just(BoardDto.builder()
                        .id(board.getId())
                        .title(board.getTitle())
                        .writer(board.getWriter())
                        .createdDate(board.getCreatedDate())
                        .viewCount(board.getViewCount())
                        .build()));
    }
    @Override
    public Mono<Board> save(BoardDto boardDto, Flux<FilePart> fileParts) {
        return fileParts
                .flatMap(filePart -> filePart.content()
                        .map(this::toByteArray)
                        .reduce(this::concatArrays)
                        .map(bytes -> File.builder()
                                .fileName(filePart.filename())
                                .fileData(bytes)
                                .fileType(Objects.requireNonNull(filePart.headers().getContentType()).toString())
                                .build()))
                .collectList()
                .flatMap(files ->
                        boardRepository.save(Board.builder()
                                .title(boardDto.getTitle())
                                .writer(boardDto.getWriter())
                                .content(boardDto.getContent())
                                .files(files) // List<File>로 저장
                                .build()));
    }
    @Override
    public Mono<Board> findById(String id) {
        return boardRepository.findById(id);
    }
    @Override
    public Mono<ResponseEntity<ByteArrayResource>> downloadFile(String id, String fileName) {
        return boardRepository.findById(id)
                .map(board -> {
                    File file = board.getFiles().stream()
                            .filter(f -> f.getFileName().equals(fileName))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("File not found"));
                    ByteArrayResource resource = new ByteArrayResource(file.getFileData());
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                            .contentType(MediaType.parseMediaType(file.getFileType()))
                            .body(resource);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    private byte[] toByteArray(DataBuffer dataBuffer) {
        byte[] bytes = new byte[dataBuffer.readableByteCount()];
        dataBuffer.read(bytes);
        DataBufferUtils.release(dataBuffer);
        return bytes;
    }
    private byte[] concatArrays(byte[] array1, byte[] array2) {
        byte[] result = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, result, 0, array1.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }
}
