package site.lawmate.user.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;
import site.lawmate.user.domain.vo.Registration;
import site.lawmate.user.domain.vo.Role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Component
@Getter
@Builder(toBuilder = true)
@ToString(exclude = {"id"})
@Setter
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String email;

    @NotNull
    private String name;

    private String password;

    @NotNull
    private String profile;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;
    private String phone;
    private String age;
    private String gender;
    private Long point = 0L;

    @Enumerated(EnumType.STRING)
    private Registration registration;

    @Builder
    public User(String name, String email, String profile, Registration registration, Role role) {
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.registration = registration;
        this.role = role;
    }

    public User update(String name, String profile) {
        this.name = name;
        this.profile = profile;
        return this;
    }

    @OneToMany(mappedBy = "writer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Question> questions;


    @OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<UserPayment> payments = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Issue> issues;
}