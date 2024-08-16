package site.lawmate.lawyer.domain.dto;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.stereotype.Component;
import site.lawmate.lawyer.domain.model.File;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Component
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostDto {
    private String id;
    private String title;
    private String content;
    private String category;
    private String lawyerId;
    private List<String> fileUrls;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
