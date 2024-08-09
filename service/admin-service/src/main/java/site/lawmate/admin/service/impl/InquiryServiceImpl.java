package site.lawmate.admin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.lawmate.admin.domain.model.Inquiry;
import site.lawmate.admin.repository.InquiryRepository;
import site.lawmate.admin.service.InquiryService;

@Slf4j
@RequiredArgsConstructor
@Service
public class InquiryServiceImpl implements InquiryService {
    private final InquiryRepository inquiryRepository;
    @Override
    public Mono<String> sendInquiry(Inquiry inquiry) {
        return inquiryRepository.save(inquiry)
                .then(Mono.just("문의가 성공적으로 전송되었습니다."))
                .switchIfEmpty(Mono.just("문의 전송에 실패했습니다."));
    }

    @Override
    public Mono<Inquiry> getInquiry(String id) {
        return inquiryRepository.findById(id);
    }

    @Override
    public Flux<Inquiry> getInquiries(PageRequest pageRequest) {
        return inquiryRepository.findAllBy(pageRequest);
    }
}
