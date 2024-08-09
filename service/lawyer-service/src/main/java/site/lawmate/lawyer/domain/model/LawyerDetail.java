package site.lawmate.lawyer.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "lawyer_details")
@Builder
@AllArgsConstructor
@ToString(exclude = "id")
@NoArgsConstructor
public class LawyerDetail implements Persistable<String> {
    @Id
    String id;
    String belong; // 소속
    String address; // 소속 주소
    String addressDetail; // 상세주소
    String belongPhone; // 소속 전화번호
    List<String> law; // 법
    String visitCost; // 방문상담비용 default : 분당 3000
    String phoneCost; // 전화상담비용 default : 분당 1500
    String videoCost; // 영상상담비용 default : 분당 2000
    String university; // 대학
    String major; // 학과
    String time; // 시간
    @Builder.Default
    Boolean premium = false; // 프리미엄 default : false.  true여야 프리미엄 계정

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdDate;
    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime modifiedDate;

    @Override
    public boolean isNew() {
        return createdDate == null;
    }
}
