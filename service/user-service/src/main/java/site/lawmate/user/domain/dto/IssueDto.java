package site.lawmate.user.domain.dto;

import lombok.*;
import org.springframework.stereotype.Component;
import site.lawmate.user.domain.model.User;

@NoArgsConstructor
@AllArgsConstructor
@Component
@Data
@Builder
@Getter
public class IssueDto {
    private Long id;
    private String law;
    private String title;
    private String content;
    private String date;
    private String time;
    private String lawyer;
    private User client;
    private String regDate;
    private String modDate;
}
