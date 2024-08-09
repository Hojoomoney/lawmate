package site.lawmate.lawyer.domain.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;


@Getter
@NoArgsConstructor
@Component
@Setter
public class PrincipalUserDetails implements Serializable {
    private UserModel user;
    private Map<String, Object> attributes;

    public PrincipalUserDetails(UserModel lawyer) {
        this.user = lawyer;
    }

    public PrincipalUserDetails(UserModel lawyer, Map<String, Object> attributes) {
        this.user = lawyer;
        this.attributes = attributes;
    }

}
