package site.lawmate.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.lawmate.admin.service.impl.VisitorServiceImpl;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/visit")
public class VisitorController {

    private final VisitorServiceImpl visitorServiceImpl;

    @PostMapping("/save") //
    public ResponseEntity<Mono<Long>> visit() {
        return ResponseEntity.ok(visitorServiceImpl.incrementVisitorCount());
    }

    @GetMapping(value = "/visitors", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<Flux<String>> getVisitorCountStream() {
        return ResponseEntity.ok(visitorServiceImpl.getVisitorCountStream());
    }

    @GetMapping("/month")
    public ResponseEntity<Mono<Long>> getVisitorCountByMonth(@RequestParam("year") String year,@RequestParam("month") String month) {
        return ResponseEntity.ok(visitorServiceImpl.getVisitorCountByMonth(year, month));
    }

    @GetMapping("/year")
    public ResponseEntity<Mono<Map<String, Long>>> getVisitorCountYearByMonth(@RequestParam("year") String year) {
        return ResponseEntity.ok(visitorServiceImpl.getVisitorCountYearByMonth(year));
    }

    @GetMapping("/last7days")
    public ResponseEntity<Mono<Map<String, Long>>> getVisitorCountByLast7Days() {
        return ResponseEntity.ok(visitorServiceImpl.getVisitorCountByLast7Days());
    }

}
