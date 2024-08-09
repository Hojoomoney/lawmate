package site.lawmate.admin.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inquiry {

    @Id
    private String id;
    private String title;
    private String content;
    private String email;
    private String category;
}
