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
public class CaseLaw {
    @Id
    private String serialNumber; // 일렬번호
    @Column(columnDefinition = "TEXT")
    private String caseName; // 사건명
    private String caseNumber; // 사건번호
    private String dateOfDecision; // 판결일자

}
