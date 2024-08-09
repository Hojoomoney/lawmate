package site.lawmate.user.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Entity(name = "premiums")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Component
@Getter
@Builder(toBuilder = true)
@ToString(exclude = {"id"})
@Setter
public class Premium {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String plan;
    private String price;
    private LocalDateTime startDate;
    private LocalDateTime expireDate;
    private String lawyer;


    //만료 여부 확인
    @Builder.Default
    private boolean isExpired = false;
}
