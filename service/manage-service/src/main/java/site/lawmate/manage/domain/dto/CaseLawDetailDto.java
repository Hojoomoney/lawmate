package site.lawmate.manage.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import site.lawmate.manage.domain.model.CaseLaw;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CaseLawDetailDto {
    private Long id;
    private CaseLaw caseLaw; // 일렬번호를 참조하는 외래 키
    private String court; // 법원명
    private String caseType; // 사건종류명
    private String summary; // 판결요지
    private String detail; // 판결내용
}
