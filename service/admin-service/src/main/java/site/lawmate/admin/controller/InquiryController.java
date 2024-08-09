package site.lawmate.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.lawmate.admin.domain.model.Inquiry;
import site.lawmate.admin.service.InquiryService;
//젠킨스
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/inquiry")
public class InquiryController {
    private final InquiryService inquiryService;

    @PostMapping("/send")
    public Mono<String> sendInquiry(@RequestBody Inquiry inquiry) {
        return inquiryService.sendInquiry(inquiry);
    }
    @GetMapping("/{id}")
    public Mono<Inquiry> getInquiry(@PathVariable String id) {
        return inquiryService.getInquiry(id);
    }

    @GetMapping("/all")
    public Flux<Inquiry> getInquiries(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return inquiryService.getInquiries(PageRequest.of(page, size));
    }
}
