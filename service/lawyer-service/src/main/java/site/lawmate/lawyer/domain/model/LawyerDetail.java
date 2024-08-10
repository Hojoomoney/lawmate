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
    String belong; // 소속 ex) 강남변호사협회, 수원변호사협회...
    String address; // 소속 주소 ex) 서울특별시 강남구 테헤란로 123 ...
    String addressDetail; // 상세주소 ex) 123동 123호 ...
    String belongPhone; // 소속 전화번호 ex) 02-1234-1234 ...
    List<String> law; // 법 종류 : 형사법 공법 국제법 국제거래법 노동법 조세법 지적재산권법 민사법 경제법 환경법
    String visitCost; // 방문상담비용 default : 분당 3000
    String phoneCost; // 전화상담비용 default : 분당 1500
    String videoCost; // 영상상담비용 default : 분당 2000
    String university; // 대학 ex) 서울대학교, 부산대학교 ...
    String major; // 학과 ex) 법학과, 경제학과 ...
    String time; // 모든 데이터 09:00 ~ 18:00로 추가
    @Builder.Default
    Boolean premium = false;

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
