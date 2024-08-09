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
import site.lawmate.user.domain.dto.UserPaymentDto;
import site.lawmate.user.domain.vo.PaymentStatus;
import site.lawmate.user.repository.UserPaymentRepository;
import site.lawmate.user.service.UserPaymentService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/user/payments")
@Slf4j
@Controller
@RequiredArgsConstructor
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
})
public class UserPaymentController {
    private final UserPaymentService userPaymentService;
    private final UserPaymentRepository userPaymentRepository;

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
    public ResponseEntity<Messenger> savePayment(@RequestBody UserPaymentDto dto) {
        log.info("payment save 파라미터: {} ", dto);
        return ResponseEntity.ok(userPaymentService.save(dto));
    }

    @PostMapping("/usePoint")
    public ResponseEntity<Messenger> usePoint(@RequestBody UserPaymentDto dto, @RequestParam String impUid) {
        log.info("payment usePoint id, impUid: {} {} ", dto, impUid);
        return ResponseEntity.ok(userPaymentService.subtractUserPoints(dto, impUid));
    }

    @PostMapping("/cancel")
    public ResponseEntity<Messenger> cancelPayment(@RequestBody UserPaymentDto dto) throws IamportResponseException, IOException {
        log.info("결제 취소 진입 성공: {}", dto);
        IamportResponse<Payment> response = iamportClient.paymentByImpUid(dto.getImpUid());
        log.info("결제 취소 imp_uid {}", response.getResponse().getImpUid());
        return ResponseEntity.ok(userPaymentService.cancelPayment(dto));
    }

    @PostMapping("/status")
    public ResponseEntity<String> paymentStatus(@RequestBody PaymentStatus status) {
        log.info("payment status: {}", status);
        if (status == PaymentStatus.PENDING) {
            return new ResponseEntity<>("Payment SUCCESS", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Payment FAILURE", HttpStatus.BAD_REQUEST);
        }
    }

    //결제 조회
    @PostMapping("/verifyIamport/{imp_uid}")
    public ResponseEntity<?> paymentByImpUid(@PathVariable("imp_uid") String imp_uid) throws IamportResponseException, IOException {
        log.info("imp_uid={}", imp_uid);
        IamportResponse<Payment> response = iamportClient.paymentByImpUid(imp_uid);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{imp_uid}/balance")
    public ResponseEntity<?> getBalance(@PathVariable String imp_uid) throws IamportResponseException, IOException {
        IamportResponse<Payment> response = iamportClient.paymentByImpUid(imp_uid);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<UserPaymentDto>> findById(@PathVariable("id") Long id) {
        log.info("payment 정보 조회 진입 id: {} ", id);
        return ResponseEntity.ok(userPaymentService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Messenger> update(@RequestBody UserPaymentDto dto) {
        log.info("update payment 진입 성공: {} ", dto.toString());
        return ResponseEntity.ok(userPaymentService.update(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Messenger> delete(@PathVariable("id") Long id) {
        log.info("delete payment id: {} ", id);
        return ResponseEntity.ok(userPaymentService.delete(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<UserPaymentDto>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) throws SQLException {
        log.info("findAll payment 진입 성공");
        return ResponseEntity.ok(userPaymentService.findAll(PageRequest.of(page, size)));
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<UserPaymentDto>> findByBuyerId(@PathVariable("buyerId") Long buyerId) {
        log.info("payment 구매자 결제 정보 조회 id: {} ", buyerId);
        return ResponseEntity.ok(userPaymentService.findByBuyerId(buyerId));
    }

    @PutMapping("/confirm")
    public ResponseEntity<Messenger> confirmPayment(@RequestBody UserPaymentDto dto) {
        log.info("결제 수락 진입: {}", dto);
        return ResponseEntity.ok(userPaymentService.confirmPayment(dto));
    }

    @GetMapping(path = "/findLawyer/{lawyer}")
    public ResponseEntity<Optional<UserPaymentDto>> findByLawyer(@PathVariable("lawyer") String lawyer) {
        log.info("premium 변호사 결제 정보 조회 id: {} ", lawyer);
        return ResponseEntity.ok(userPaymentService.findByLawyer(lawyer));
    }

}
