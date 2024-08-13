package site.lawmate.admin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.lawmate.admin.domain.model.Admin;
import site.lawmate.admin.domain.dto.AdminDto;
import site.lawmate.admin.domain.vo.ExceptionStatus;
import site.lawmate.admin.handler.LoginException;
import site.lawmate.admin.repository.AdminRepository;
import site.lawmate.admin.service.AdminService;


@Slf4j
@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public Mono<Object> save(AdminDto adminDto) {
            return adminRepository.findByEmail(adminDto.getEmail())
                .flatMap(existingAdmin -> Mono.error(new LoginException(ExceptionStatus.ALREADY_EXISTS, "이미 존재하는 이메일입니다.")))
                .switchIfEmpty(Mono.defer(() -> {
                    Admin admin = Admin.builder()
                            .email(adminDto.getEmail())
                            .password(adminDto.getPassword())
                            .name(adminDto.getName())
                            .role(adminDto.getRole())
                            .enabled(adminDto.getEnabled())
                            .build();
                    return adminRepository.save(admin);
                }));
    }


    @Override
    public Mono<Admin> findById(String id) {
        return adminRepository.findById(id);
    }

    @Override
    public Flux<Admin> findAll() {
        return adminRepository.findAll();
    }

    @Override
    public Mono<Admin> update(String id, AdminDto adminDto) {
        return adminRepository.findById(id)
                .map(admin -> {
                    admin.setEmail(adminDto.getEmail());
                    admin.setPassword(adminDto.getPassword());
                    admin.setName(adminDto.getName());
                    return admin;
                })
                .flatMap(adminRepository::save);
    }

    @Override
    public Mono<Void> delete(String id) {
        return adminRepository.deleteById(id);
    }

    @Override
    public Mono<String> switching(String id) {
        return adminRepository.findById(id)
                .map(admin -> {
                    admin.setEnabled(!admin.getEnabled());
                    return admin;
                })
                .flatMap(adminRepository::save)
                .flatMap(admin -> Mono.just("Switch Success"))
                .switchIfEmpty(Mono.just("Switch Failure"));
    }


    @Override
    public Flux<Admin> findAllByEnabled() {
        return adminRepository.findAll().filter(admin -> !admin.getEnabled());
    }

    @Override
    public Mono<Long> countAdminsEnabledFalse() {
        return adminRepository.findAll().filter(admin -> !admin.getEnabled()).count();
    }

    @Override
    public Flux<AdminDto> searchByName(String keyword) {
        return adminRepository.findAll()
                .filter(admin -> admin.getName().contains(keyword) || admin.getEmail().contains(keyword))
                .filter(Admin::getEnabled)
                .flatMap(admin -> Flux.just(AdminDto.builder()
                        .email(admin.getEmail())
                        .name(admin.getName())
                        .role(admin.getRole())
                        .build()));
    }

}
