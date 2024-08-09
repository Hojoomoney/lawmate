package site.lawmate.user.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.lawmate.user.component.Messenger;
import site.lawmate.user.domain.dto.UserDto;
import site.lawmate.user.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
})
public class UserController {
    private final UserService service;

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "100") int size
    ) {
        log.info("유저 전체 조회 진입 page: {} size: {}", page, size);
        return ResponseEntity.ok(service.findAll(PageRequest.of(page, size)));
    }

    @GetMapping("/searchEmail")
    public ResponseEntity<Boolean> existsByEmail(@RequestParam("email") String email) {
        log.info("유저 search email: {}", email);
        Boolean flag = service.existsByUsername(email);
        log.info("existsEmail : " + email);
        return ResponseEntity.ok(flag);
    }

    @GetMapping("/findEmail")
    public ResponseEntity<Optional<UserDto>> findByEmail(@RequestParam("email") String email) {
        log.info("유저 findByEmail: {}", email);
        return ResponseEntity.ok(service.findByEmail(email));
    }

    @GetMapping("/search")
    public ResponseEntity<Boolean> existsById(@RequestParam("id") Long id) {
        return ResponseEntity.ok(service.existsById(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<UserDto>> findById(@PathVariable("id") Long id) {
        log.info("유저 findById: {}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Messenger> update(@RequestBody UserDto dto) {
        log.info("유저 update dto: {}", dto);
        return ResponseEntity.ok(service.update(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Messenger> delete(@PathVariable("id") Long id) {
        log.info("유저 delete id: {}", id);
        return ResponseEntity.ok(service.delete(id));
    }

    @GetMapping("/logout")
    public ResponseEntity<Boolean> logout(@RequestHeader("authorization") String accessToken) {
        var flag = service.logout(accessToken);
        return ResponseEntity.ok(flag);
    }
}