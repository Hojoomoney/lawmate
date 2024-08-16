package site.lawmate.user.controller;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.lawmate.user.component.Messenger;
import site.lawmate.user.domain.dto.LawPaymentDto;
import site.lawmate.user.domain.vo.PaymentStatus;
import site.lawmate.user.service.LawPaymentService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/law/payments")
@Slf4j
@Controller
@RequiredArgsConstructor
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
})
public class LawPaymentController {
    private final LawPaymentService paymentService;

    @Value("${iamport.key}")
    private String apiKey;
    @Value("${iamport.secret}")
    private String apiSecret;

    private IamportClient iamportClient;

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(apiKey, apiSecret);
    }

    @PostMapping("/save")
    public ResponseEntity<Messenger> savePayment(@RequestBody LawPaymentDto dto) {
        log.info("premium 결제 요청 파라미터: {}", dto);
        return ResponseEntity.ok(paymentService.save(dto));
    }

    @PostMapping("/status")
    public ResponseEntity<String> paymentStatus(@RequestBody PaymentStatus status) {
        log.info("premium 결제 status: {}", status);
        if (status == PaymentStatus.OK) {
            return new ResponseEntity<>("Payment SUCCESS", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Payment FAILURE", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/verifyIamport/{imp_uid}")
    public ResponseEntity<?> paymentByImpUid(@PathVariable("imp_uid") String imp_uid) throws IamportResponseException, IOException {
        log.info("imp_uid={}", imp_uid);
        IamportResponse<Payment> response = iamportClient.paymentByImpUid(imp_uid);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<LawPaymentDto>> findById(@PathVariable("id") Long id) {
        log.info("premium 결제 정보 조회 진입 id: {} ", id);
        return ResponseEntity.ok(paymentService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Messenger> update(@RequestBody LawPaymentDto dto) {
        log.info("premium 결제 update 진입 성공: {} ", dto.toString());
        return ResponseEntity.ok(paymentService.update(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Messenger> delete(@PathVariable("id") Long id) {
        log.info("delete payment id: {} ", id);
        return ResponseEntity.ok(paymentService.delete(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<LawPaymentDto>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("premium 결제 전체 조회 진입 page: {} size: {}", page, size);
        return ResponseEntity.ok(paymentService.findAll(PageRequest.of(page, size)));
    }

    @GetMapping(path = "/findLawyer/{lawyer}")
    public ResponseEntity<Optional<LawPaymentDto>> findByLawyer(@PathVariable("lawyer") String lawyer) {
        log.info("premium 결제 정보 조회 진입 유저 id: {} ", lawyer);
        return ResponseEntity.ok(paymentService.findByLawyer(lawyer));
    }
}
