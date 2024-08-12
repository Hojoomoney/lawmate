package site.lawmate.lawyer.domain.dto;

import lombok.*;
import org.springframework.stereotype.Component;
import site.lawmate.lawyer.domain.model.LawyerDetail;

import java.time.LocalDateTime;

@Data
@Component
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LawyerDto {
    private String id;
    private String username;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String birth;
    private String lawyerNo;
    private String mid;
    private Boolean auth;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    private LawyerDetail detail;
}
