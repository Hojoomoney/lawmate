package site.lawmate.admin.service;

import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.lawmate.admin.domain.model.Inquiry;

public interface InquiryService {

    Mono<String> sendInquiry(Inquiry inquiry);
    Mono<Inquiry> getInquiry(String id);
    Flux<Inquiry> getInquiries(PageRequest pageRequest);
}
