package site.lawmate.user.domain.dto;

import lombok.Getter;
import site.lawmate.user.domain.model.User;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

    private String name;
    private String email;
    private String profile;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.profile = user.getProfile();
    }
}
