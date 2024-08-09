package site.lawmate.user.domain.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "issues")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
@ToString(exclude = {"id"})
@Slf4j
public class Issue extends BaseEntity {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String law;
    private String title;
    private String content;
    private String date;
    private String time;
    private String lawyer;

    @ManyToOne
    @JoinColumn
    private User client;


    public static Issue of(String law, String title, String content, User client, String lawyer) {
        Issue issue = new Issue();
        issue.law = law;
        issue.title = title;
        issue.content = content;
        issue.client = client;
        issue.lawyer = lawyer;
        return issue;
    }
}
