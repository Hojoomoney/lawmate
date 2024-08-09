package site.lawmate.manage.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserStatsDto {

    private LocalDate date; // 날짜
    private Long newUserCount; // 신규 가입자 수
    private Long increaseRate; // 증가율
    private Integer month;
    private Integer year;
    private Integer week;
    private Long increaseRateAverage;

}
