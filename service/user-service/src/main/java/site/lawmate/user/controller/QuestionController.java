package site.lawmate.user.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.lawmate.user.component.Messenger;
import site.lawmate.user.domain.dto.QuestionDto;
import site.lawmate.user.service.QuestionService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/questions")
@Slf4j
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
})
public class QuestionController {
    private final QuestionService service;

    @SuppressWarnings("static-access")
    @PostMapping("/save")
    public ResponseEntity<Messenger> save(@RequestBody QuestionDto dto) throws SQLException {
        log.info("question save 파라미터: {} ", dto);
        return ResponseEntity.ok(service.save(dto));
    }

    @GetMapping("/all")
    public ResponseEntity<List<QuestionDto>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "30") int size) {
        return ResponseEntity.ok(service.findAll(PageRequest.of(page, size)));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Messenger> delete(@PathVariable("id") Long id) throws SQLException {
        return ResponseEntity.ok(service.delete(id));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Messenger> update(@RequestBody QuestionDto dto) {
        return ResponseEntity.ok(service.update(dto));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Optional<QuestionDto>> findById(@PathVariable("id") Long id) throws SQLException {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/total")
    public ResponseEntity<Messenger> count() throws SQLException {
        return ResponseEntity.ok(service.count());
    }

    @GetMapping("/search")
    public ResponseEntity<Boolean> existsById(@RequestParam("id") Long id) throws SQLException {
        return ResponseEntity.ok(service.existsById(id));
    }


}