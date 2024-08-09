package site.lawmate.admin.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.lawmate.admin.domain.dto.BoardDto;
import site.lawmate.admin.domain.model.Board;

public interface BoardService {
    Flux<BoardDto> findAll(PageRequest pageRequest);
    Mono<Board> save(BoardDto boardDto, Flux<FilePart> fileParts);
    Mono<Board> findById(String id);
    Mono<ResponseEntity<ByteArrayResource>> downloadFile(String id, String fileName);
}
