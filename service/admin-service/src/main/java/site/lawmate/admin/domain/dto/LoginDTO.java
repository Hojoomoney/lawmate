package site.lawmate.admin.domain.dto;

import lombok.Data;

@Data
public class LoginDTO {
    private String email;
    private String password;
}
