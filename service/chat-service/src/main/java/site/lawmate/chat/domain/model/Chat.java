package site.lawmate.chat.domain.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document("chats")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    @Id
    private ObjectId id; // 고유 식별자

    // 로그인시 과거 채팅방 기록 나옴
    private Long userId;
    private Long chattingRoomId;

    //질문과 대답
    private String question;
    private String answer;

    //채팅 인덱스
    private Integer messageOrder;
    @Builder.Default
    private LocalDateTime chatDate = LocalDateTime.now(); //필드 초기화
}
