package site.lawmate.user.service.impl;

import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.lawmate.user.component.Messenger;
import site.lawmate.user.domain.dto.LawPaymentDto;
import site.lawmate.user.domain.dto.UserPaymentDto;
import site.lawmate.user.domain.model.LawPayment;
import site.lawmate.user.domain.model.PaymentCallbackRequest;
import site.lawmate.user.domain.vo.PaymentStatus;
import site.lawmate.user.repository.LawPaymentRepository;
import site.lawmate.user.service.LawPaymentService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LawPaymentServiceImpl implements LawPaymentService {
    private final LawPaymentRepository payRepository;

    @Transactional
    @Override
    public Messenger save(LawPaymentDto dto) {
        log.info("premium 결제 service 진입 성공: {}", dto);
        LawPayment payment = dtoToEntity(dto);
        LawPayment savedPayment = payRepository.save(payment);
        boolean exists = payRepository.existsById(savedPayment.getId());
        if (exists) {
            savedPayment.setStatus(PaymentStatus.OK);
            payRepository.save(savedPayment);
        }
        return Messenger.builder()
                .message(exists ? "SUCCESS" : "FAILURE")
                .build();
    }

    @Transactional
    @Override
    public Messenger delete(Long id) {
        Optional<LawPayment> payment = payRepository.findById(id);
        if (payment.isPresent()) {
            payRepository.deleteById(id);
            return Messenger
                    .builder()
                    .message("SUCCESS")
                    .build();
        } else {
        throw new EntityNotFoundException("Payment not found with id: " + id);
        }
    }
    @Override
    public Messenger update(LawPaymentDto lawPaymentDto) {
        Optional<LawPayment> payment = payRepository.findById(lawPaymentDto.getId());
        if (payment.isPresent()) {
            LawPayment entity = payment.get();
            LawPayment updated = entity.toBuilder()
                    .premium(lawPaymentDto.getPremium())
                    .lawyer(lawPaymentDto.getLawyer())
                    .amount(lawPaymentDto.getAmount())
                    .impUid(lawPaymentDto.getImpUid())
                    .build();
            Long updatedId = payRepository.save(updated).getId();
            return Messenger.builder()

                    .message("SUCCESS ID is " + updatedId)
                    .build();
        } else {
            return Messenger.builder()
                    .message("FAILURE")
                    .build();
        }
    }

    @Override
    public UserPaymentDto findRequestDto(String impUid) {
        return null;
    }

    @Override
    public IamportResponse<Payment> paymentByCallback(PaymentCallbackRequest request) {
        return null;
    }

    @Override
    public Optional<LawPaymentDto> findByLawyer(String lawyer) {
        return payRepository.findByLawyer(lawyer)
                .map(this::entityToDto);
    }

    @Override
    public List<LawPaymentDto> findAll(PageRequest pageRequest) {
        return payRepository.findAll(pageRequest).stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<LawPaymentDto> findById(Long id) {
        Optional<LawPayment> payment = payRepository.findById(id);
        return payment.map(this::entityToDto);
    }

    @Override
    public Messenger count() {
        return Messenger.builder()
                .message(payRepository.count() + "").build();
    }

    @Override
    public boolean existsById(Long id) {
        return payRepository.existsById(id);
    }

}
