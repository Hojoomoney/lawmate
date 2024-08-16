package site.lawmate.user.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;
import site.lawmate.user.domain.vo.PaymentStatus;

import java.time.LocalDateTime;

@Entity(name = "law_payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Component
@Getter
@Builder(toBuilder = true)
@ToString(exclude = {"id"})
@Setter
public class LawPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String lawyer;
    private String impUid;
    private LocalDateTime startDate;
    private LocalDateTime expireDate;

    //만료 여부 확인
    @Builder.Default
    private boolean isExpired = false;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private Long amount;

    @ManyToOne
    @JoinColumn
    private Premium premium;

}
