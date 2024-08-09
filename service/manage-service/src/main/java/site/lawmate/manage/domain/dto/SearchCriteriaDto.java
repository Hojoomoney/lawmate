package site.lawmate.manage.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteriaDto {
    private String serialNumber;
    private String caseNumber;
    private String caseName;
}
