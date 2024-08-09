package site.lawmate.user.domain.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import site.lawmate.user.domain.vo.PaymentStatus;

@Entity(name = "user_payments")
@Getter
@NoArgsConstructor
@Component
@Setter
public class UserPayment extends BaseEntity {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String lawyer;
    private String impUid; //결제 고유 번호
    private Long amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Builder
    public UserPayment(Long id, String lawyer, String impUid, PaymentStatus status, User buyer, Product product, Long amount) {
        this.id = id;
        this.lawyer = lawyer;
        this.impUid = impUid;
        this.status = status;
        this.buyer = buyer;
        this.product = product;
        this.amount = amount;
    }

    @ManyToOne
    @JoinColumn
    private Product product;

    @ManyToOne
    @JoinColumn
    private User buyer;
}
