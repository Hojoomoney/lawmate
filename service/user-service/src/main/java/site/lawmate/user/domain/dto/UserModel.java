package site.lawmate.user.domain.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.stereotype.Component;
import site.lawmate.user.domain.vo.Registration;
import site.lawmate.user.domain.vo.Role;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class UserModel {
    private String id;
    private String name;
    private String email;
    private List<Role> roles;

    @Enumerated(EnumType.STRING)
    private Registration registration;
}