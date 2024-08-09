package site.lawmate.chat.domain.dto;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Component
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {
    private ObjectId id;
    private Long userId;
    private Long chattingRoomId;
    private String question;
    private String answer;
    private Integer messageOrder;
    private LocalDateTime chatDate;
    private String chatName;
    private LocalDateTime recentChat;
    private LocalDateTime oldestChatDate;
}
