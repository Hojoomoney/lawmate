package site.lawmate.user.controller;

import com.siot.IamportRestClient.IamportClient;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.lawmate.user.component.Messenger;
import site.lawmate.user.domain.dto.PremiumDto;
import site.lawmate.user.service.PremiumService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/premium")
@Slf4j
@Controller
@RequiredArgsConstructor
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
})
public class PremiumController {
    private final PremiumService premiumService;

    @Value("${iamport.key}")
    private String apiKey;
    @Value("${iamport.secret}")
    private String apiSecret;

    private IamportClient iamportClient;

    @PostMapping("/save")
    public ResponseEntity<Messenger> savePremium(@RequestBody PremiumDto dto) {
        log.info("premium save 파라미터: {}", dto);
        return ResponseEntity.ok(premiumService.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<PremiumDto>> findById(@PathVariable("id") Long id) {
        log.info("premium 정보 조회 진입 id: {} ", id);
        return ResponseEntity.ok(premiumService.findById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<PremiumDto>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("premium 전체 조회 진입 page: {} size: {}", page, size);
        return ResponseEntity.ok(premiumService.findAll(PageRequest.of(page, size)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Messenger> update(@RequestBody PremiumDto dto) {
        log.info("update premium 진입 성공: {} ", dto.toString());
        return ResponseEntity.ok(premiumService.update(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Messenger> delete(@PathVariable("id") Long id) {
        log.info("delete premium id: {} ", id);
        return ResponseEntity.ok(premiumService.delete(id));
    }

}
