package site.lawmate.admin.domain.dto;

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

    public PrincipalUserDetails(UserModel admin) {
        this.user = admin;
    }

    public PrincipalUserDetails(UserModel admin, Map<String, Object> attributes) {
        this.user = admin;
        this.attributes = attributes;
    }

}











