package site.lawmate.lawyer.domain.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Component
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LawyerDetailDto {
    private String id;
    private String belong;
    private String address;
    private String addressDetail;
    private String belongPhone;
    private List<String> law;
    private String visitCost;
    private String phoneCost;
    private String videoCost;
    private String university;
    private String time;
    private String major;
    private Boolean premium;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}