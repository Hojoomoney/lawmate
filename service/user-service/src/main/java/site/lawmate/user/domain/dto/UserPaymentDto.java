package site.lawmate.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import site.lawmate.user.domain.model.Product;
import site.lawmate.user.domain.model.User;
import site.lawmate.user.domain.vo.PaymentStatus;

@NoArgsConstructor
@AllArgsConstructor
@Component
@Builder
@Data
public class UserPaymentDto {
    private Long id;
    private String lawyer;
    private String impUid;
    private PaymentStatus status;
    private Long amount;

    private User buyer;
    private Product product;
}
