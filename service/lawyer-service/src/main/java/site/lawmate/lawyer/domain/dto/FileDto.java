package site.lawmate.lawyer.domain.dto;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@Component
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FileDto {
    private String id;
    private String filename;
    private String contentType;
    private String url;
    private String lawyerId;
    private String postId;
    LocalDateTime createdDate;
    LocalDateTime modifiedDate;
}
