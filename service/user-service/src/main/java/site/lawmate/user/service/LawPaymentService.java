package site.lawmate.user.service;

import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import site.lawmate.user.component.Messenger;
import site.lawmate.user.domain.dto.LawPaymentDto;
import site.lawmate.user.domain.dto.UserPaymentDto;
import site.lawmate.user.domain.model.LawPayment;
import site.lawmate.user.domain.model.PaymentCallbackRequest;

import java.util.List;
import java.util.Optional;

public interface LawPaymentService extends CommandService<LawPaymentDto>, QueryService<LawPaymentDto> {
    // 결제 요청 데이터 조회
    UserPaymentDto findRequestDto(String orderUid);

    // 결제(콜백)
    IamportResponse<Payment> paymentByCallback(PaymentCallbackRequest request);

    Optional<LawPaymentDto> findByLawyer(String lawyer);

    default LawPayment dtoToEntity(LawPaymentDto dto) {
        return LawPayment.builder()
                .lawyer(dto.getLawyer())
                .impUid(dto.getImpUid())
                .amount(dto.getAmount())
                .startDate(dto.getStartDate())
                .expireDate(dto.getExpireDate())
                .premium(dto.getPremium())
                .build();
    }

    default LawPaymentDto entityToDto(LawPayment pay) {
        return LawPaymentDto.builder()
                .id(pay.getId())
                .lawyer(pay.getLawyer())
                .impUid(pay.getImpUid())
                .amount(pay.getAmount())
                .startDate(pay.getStartDate())
                .expireDate(pay.getExpireDate())
                .premium(pay.getPremium())
                .build();
    }

}
