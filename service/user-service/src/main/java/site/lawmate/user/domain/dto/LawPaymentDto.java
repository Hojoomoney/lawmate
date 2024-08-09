package site.lawmate.user.domain.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import site.lawmate.user.domain.model.Premium;

@NoArgsConstructor
@AllArgsConstructor
@Component
@Builder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)public class LawPaymentDto {
    private Long id;
    private String lawyer;
    private String impUid;

    private String status;
    private Long amount;

    private Premium premium;
}
