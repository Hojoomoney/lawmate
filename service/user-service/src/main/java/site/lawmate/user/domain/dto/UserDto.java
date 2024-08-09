package site.lawmate.user.domain.dto;

import lombok.*;
import org.springframework.stereotype.Component;
import site.lawmate.user.domain.vo.Role;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Component
@Data
@Builder
@Setter
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private String password;
    private String profile;
    private Role role;
    private String phone;
    private String age;
    private String gender;
    private String regDate;
    private String modDate;
    private List<Role> roles;
    private Long point = 0L;
    private String registration;

    public UserDto(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

}
