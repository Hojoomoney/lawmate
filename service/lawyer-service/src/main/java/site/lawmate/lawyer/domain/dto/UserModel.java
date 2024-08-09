package site.lawmate.lawyer.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import site.lawmate.lawyer.domain.vo.Role;

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
}
