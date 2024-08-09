package site.lawmate.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.lawmate.user.component.Messenger;
import site.lawmate.user.domain.dto.LoginDTO;
import site.lawmate.user.domain.dto.OAuth2UserDto;
import site.lawmate.user.domain.dto.UserDto;
import site.lawmate.user.domain.dto.UserModel;
import site.lawmate.user.domain.model.PrincipalUserDetails;
import site.lawmate.user.domain.model.User;
import site.lawmate.user.domain.vo.Registration;
import site.lawmate.user.domain.vo.Role;
import site.lawmate.user.repository.UserRepository;
import site.lawmate.user.service.UserService;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public Messenger save(UserDto dto) {
        log.info("service 진입 파라미터: {} ", dto);
        dto.setRegistration(Registration.LOCAL.name());
        log.info("Registration: {}", dto.getRegistration());
        User user = dtoToEntity(dto);
        User savedUser = userRepository.save(user);
        return Messenger.builder()
                .message(userRepository.existsById(savedUser.getId()) ? "SUCCESS" : "FAILURE")
                .build();
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        return userRepository.findByEmail(email).map(this::entityToDto);

    }

    @Override
    public LoginDTO oauthJoin(OAuth2UserDto dto) {
        User oauthUser = User.builder()
                .email(dto.email())
                .name(dto.name())
                .profile(dto.profile())
                .registration(Registration.valueOf(Registration.GOOGLE.name()))
                .build();
        if (userRepository.existsByEmail(oauthUser.getEmail()) > 0) {
            User existOauthUpdate = userRepository.findByEmail(dto.email())
                    .stream()
                    .findFirst()
                    .get();
            return LoginDTO.builder()
                    .user(UserDto.builder()
                            .id(existOauthUpdate.getId())
                            .email(existOauthUpdate.getEmail())
                            .roles(List.of(Role.ROLE_USER))
                            .build())
                    .build();
        } else {
            var newOauthSave = userRepository.save(oauthUser);
            return LoginDTO.builder()
                    .user(UserDto.builder()
                            .id(newOauthSave.getId())
                            .email(newOauthSave.getEmail())
                            .roles(List.of(Role.ROLE_NEWUSER))
                            .build())
                    .build();
        }
    }
    @Transactional
    @Override
    public PrincipalUserDetails login(LoginDTO dto){
        log.info("login 진입 성공 email: {}", dto.getEmail());
        Optional<User> optionalUser = userRepository.findByEmail(dto.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            boolean flag = user.getPassword().equals(dto.getPassword());
            if (flag){
                return new PrincipalUserDetails(UserModel.builder()
                        .id(user.getId().toString())
                        .email(user.getEmail())
                        .name(user.getName())
                        .roles(List.of(Role.ROLE_USER))
                        .registration(Registration.valueOf(Registration.LOCAL.name()))
                        .build());
            } else {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
        } else{
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        }
    }

    @Transactional
    @Override
    public Messenger delete(Long id) {
        userRepository.deleteById(id);
        return Messenger.builder()
                .message(userRepository.findById(id).isPresent() ? "FAILURE" : "SUCCESS")
                .build();
    }

    @Override
    public List<UserDto> findAll(PageRequest pageRequest) {
        return userRepository.findAllByOrderByIdDesc(pageRequest).stream().map(this::entityToDto).toList();
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        return userRepository.findById(id).map(this::entityToDto);
    }

    @Override
    public Messenger count() {
        return Messenger.builder().message(userRepository.count() + "").build();
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Transactional
    @Override
    public Messenger update(UserDto dto) {
        Optional<User> optionalUser = userRepository.findById(dto.getId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            User modifyUser = user.toBuilder()
                    .phone(dto.getPhone())
                    .profile(dto.getProfile())
                    .age(dto.getAge())
                    .gender(dto.getGender())
                    .build();
            Long updateUserId = userRepository.save(modifyUser).getId();

            return Messenger.builder()
                    .message("SUCCESS ID is " + updateUserId)
                    .build();
        } else {
            return Messenger.builder()
                    .message("FAILURE")
                    .build();
        }
    }

    @Override
    public Boolean logout(String accessToken) {
        return null;
    }

    @Override
    public User autoRegister() {
        User user = User.builder()
                .email("example@example.com")
                .build();
        return userRepository.save(user);
    }

    @Override
    public Boolean existsByUsername(String email) {
        Integer count = userRepository.existsByEmail(email);
        return count == 1;
    }
}
