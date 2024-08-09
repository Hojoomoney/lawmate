package site.lawmate.lawyer.domain.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@Component
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReservationDto {
    private String id;
    private String date;
    private String time;
    private String message;
    private String status;
    private String userId;
    private String lawyerId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
