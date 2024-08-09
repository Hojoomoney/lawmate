package site.lawmate.manage.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CaseLawDto {

    private String serialNumber; // 일렬번호
    private String caseName; // 사건명
    private String caseNumber; // 사건번호
    private String dateOfDecision; // 판결일자

    private String court; // 법원명
    private String caseType; // 사건종류명
    private String summary; // 판결요지
    private String detail; // 판결내용
}
