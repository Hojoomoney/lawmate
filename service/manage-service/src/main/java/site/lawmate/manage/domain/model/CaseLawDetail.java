package site.lawmate.manage.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class CaseLawDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "serialNumber")
    private CaseLaw caseLaw; // 일렬번호를 참조하는 외래 키

    private String court; // 법원명
    private String caseType; // 사건종류명
    @Column(columnDefinition = "TEXT")
    private String summary; // 판결요지
    @Column(columnDefinition = "LongText")
    private String detail; // 판결내용
}
