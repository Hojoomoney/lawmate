package site.lawmate.user.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import site.lawmate.user.component.Messenger;
import site.lawmate.user.domain.dto.IssueDto;
import site.lawmate.user.service.IssueService;
import site.lawmate.user.service.impl.IssueServiceImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/issues")
@Slf4j
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
})
public class IssueController {
    private final IssueService issueService;
    private final IssueServiceImpl issueServiceImpl;

    @GetMapping(path = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(){
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        issueService.addEmitter(emitter);
        return emitter;
    }

    @SuppressWarnings("static-access")
    @PostMapping("/save")
    public ResponseEntity<Messenger> save(@RequestBody IssueDto dto) throws SQLException {
        log.info("issue save 파라미터: {} ", dto);
        return ResponseEntity.ok(issueService.save(dto));
    }

    @GetMapping("/notification/{lawyer}")
    public ResponseEntity<List<IssueDto>> getAllLawyerNotifications(@PathVariable("lawyer") String lawyer) throws SQLException {
        log.info("issue 변호사 알림 진입 id: {} ", lawyer);
        List<IssueDto> issues = issueService.getAllNotifications(lawyer);
        return ResponseEntity.ok(issues);
    }

    @GetMapping("/all")
    public ResponseEntity<List<IssueDto>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        log.info("issue 전체 조회 진입 page: {} size: {}", page, size);
        return ResponseEntity.ok(issueService.findAll(PageRequest.of(page, size)));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Messenger> delete(@PathVariable("id") Long id) throws SQLException {
        log.info("delete issue id: {} ", id);
        return ResponseEntity.ok(issueService.delete(id));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Messenger> update(@RequestBody IssueDto dto) {
        log.info("update issue 진입 성공: {} ", dto.toString());
        return ResponseEntity.ok(issueService.update(dto));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Optional<IssueDto>> findById(@PathVariable("id") Long id) throws SQLException {
        log.info("issue 정보 조회 진입 id: {} ", id);
        return ResponseEntity.ok(issueService.findById(id));
    }

    @GetMapping("/total")
    public ResponseEntity<Messenger> count() throws SQLException {
        log.info("issue count 진입 성공");
        return ResponseEntity.ok(issueService.count());
    }

    @GetMapping("/search")
    public ResponseEntity<Boolean> existsById(@RequestParam("id") Long id) throws SQLException {
        log.info("issue search 진입 성공 id: {} ", id);
        return ResponseEntity.ok(issueService.existsById(id));
    }
}
