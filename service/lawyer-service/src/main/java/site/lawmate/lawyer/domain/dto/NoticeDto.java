package site.lawmate.lawyer.domain.dto;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class NoticeDto {
    private String id;
    private String message;
    private String userId;
    private String lawyerId;
    private String status;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
