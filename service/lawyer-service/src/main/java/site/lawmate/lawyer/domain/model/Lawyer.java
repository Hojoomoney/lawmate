package site.lawmate.lawyer.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "lawyers")
@Builder
@AllArgsConstructor
@ToString(exclude = "id")
@NoArgsConstructor

public class Lawyer implements Persistable<String> {
    @Id
    String id;
    String email;
    String password;
    String name;
    String phone;
    String birth;
    String lawyerNo; // 8자리 랜덤숫자
    String mid; // default값 imp78717406
    @Builder.Default
    Boolean auth = false;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdDate;
    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime modifiedDate;

    LawyerDetail detail;

    @Override
    public boolean isNew() {
        return createdDate == null;
    }
}