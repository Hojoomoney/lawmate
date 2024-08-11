package site.lawmate.user.service.impl;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.lawmate.user.component.Messenger;
import site.lawmate.user.domain.dto.UserPaymentDto;
import site.lawmate.user.domain.model.PaymentCallbackRequest;
import site.lawmate.user.domain.model.User;
import site.lawmate.user.domain.model.UserPayment;
import site.lawmate.user.domain.vo.PaymentStatus;
import site.lawmate.user.repository.ProductRepository;
import site.lawmate.user.repository.UserPaymentRepository;
import site.lawmate.user.repository.UserRepository;
import site.lawmate.user.service.UserPaymentService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserPaymentServiceImpl implements UserPaymentService {
    private final UserPaymentRepository payRepository;
    private final IamportClient iamportClient;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    @Override
    public Messenger save(UserPaymentDto dto) {
        log.info("결제 service 진입 성공: {}", dto);
        UserPayment payment = dtoToEntity(dto);
        UserPayment savedPayment = payRepository.save(payment);
        boolean exists = payRepository.existsById(savedPayment.getId());
        if (exists && payment.getStatus() == PaymentStatus.OK) {
            Optional.ofNullable(payment.getBuyer())
                    .map(User::getId)
                    .ifPresent(buyerId -> {
                        Optional.ofNullable(payment.getAmount())
                                .filter(amount -> amount > 0)
                                .ifPresent(amount -> addUserPoints(buyerId, amount));
//                        subtractUserPoints(dto, payment.getImpUid());
                    });
        }
        return Messenger.builder()
                .message(exists ? "SUCCESS" : "FAILURE")
                .build();
    }

    @Override
    public void addUserPoints(Long id, Long amount) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            log.info(() -> String.format(
                    "포인트 충전 전: %d", optionalUser.get().getPoint()));
            User user = optionalUser.get();
            Long currentPoints = Optional.ofNullable(user.getPoint()).orElse(0L);
            user.setPoint(currentPoints + amount);
            userRepository.save(user);
            log.info("포인트 충전 성공: {}", user.getPoint());
        }
        Messenger.builder().message("SUCCESS").build();
    }

    @Transactional
    @Override
    public Messenger subtractUserPoints(UserPaymentDto dto, String impUid) {
        Optional<User> optionalUser = userRepository.findById(dto.getId());
        if (optionalUser.isPresent()) {
            log.info(() -> String.format(
                    "포인트 사용 전: %d", optionalUser.get().getPoint()));
            User user = optionalUser.get();
            Long currentPoints = Optional.ofNullable(user.getPoint()).orElse(0L);
            if (currentPoints < dto.getAmount()) {
                return Messenger.builder().message("FAILURE: 포인트 부족").build();
            }
            user.setPoint(currentPoints - dto.getAmount());
            userRepository.save(user);
            log.info("포인트 사용 성공: {}", user.getPoint());
            UserPayment payment = UserPayment.builder()
                    .buyer(user)
                    .amount(dto.getAmount())
                    .status(PaymentStatus.PENDING)
                    .product(dto.getProduct())
                    .impUid(impUid)
                    .build();
            UserPayment savedPayment = payRepository.save(payment);
            log.info("결제 정보 저장 성공: {}", savedPayment);
            return Messenger.builder().message("SUCCESS").build();
        }
        return Messenger.builder().message("FAILURE: User not found.").build();
    }

    @Transactional
    @Override
    public Messenger cancelPayment(UserPaymentDto dto) throws IamportResponseException, IOException {
        IamportResponse<Payment> response = iamportClient.paymentByImpUid(dto.getImpUid());
        log.info("결제 취소 service 진입 성공 imp_uid={}", response.getResponse().getImpUid());

        List<UserPayment> userPayments = payRepository.findByImpUid(response.getResponse().getImpUid());
        if (!userPayments.isEmpty()) {
            userPayments.forEach(payment -> payment.setStatus(PaymentStatus.CANCELED));
            payRepository.saveAll(userPayments);

            Optional<User> optionalUser = userRepository.findById(dto.getId());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                UserPayment firstPayment = userPayments.get(0);
                Long currentPoints = Optional.ofNullable(user.getPoint()).orElse(0L);
                user.setPoint(currentPoints + firstPayment.getAmount());
                userRepository.save(user);

                log.info("포인트 환불 성공: {}", user.getPoint());

                CancelData cancelData = createCancelData(response, Math.toIntExact(firstPayment.getAmount()));
                IamportResponse<Payment> cancelResponse = iamportClient.cancelPaymentByImpUid(cancelData);
                log.info("SUCCESS: 결제 취소 완료 {}", cancelResponse.getMessage());

                return Messenger.builder().message("SUCCESS").build();
            }
        }

        return Messenger.builder().message("FAILURE: Payment imp_uid not found or mismatch").build();
    }

    private CancelData createCancelData(IamportResponse<Payment> response, int cancelAmount) {
        if (cancelAmount == 0) {
            return new CancelData(response.getResponse().getImpUid(), true);
        }
        return new CancelData(response.getResponse().getImpUid(), true, new BigDecimal(cancelAmount));
    }

    @Transactional
    @Override
    public Messenger confirmPayment(UserPaymentDto dto) {
        Optional<UserPayment> payment = payRepository.findById(dto.getId());
        if (payment.isPresent()) {
            UserPayment userPayment = payment.get();
            userPayment.setStatus(PaymentStatus.OK);
            payRepository.save(userPayment);
            return Messenger.builder().message("SUCCESS").build();
        }
        return Messenger.builder()
                .message("FAILURE")
                .build();
    }

    @Override
    public UserPaymentDto findRequestDto(String orderUid) {
        return null;
    }

    @Override
    public IamportResponse<Payment> paymentByCallback(PaymentCallbackRequest request) {
        return null;
    }


    @Transactional
    @Override
    public Messenger delete(Long id) {
        payRepository.deleteById(id);
        return Messenger.builder()
                .message(payRepository.existsById(id) ? "FAILURE" : "SUCCESS")
                .build();
    }

    @Override
    public List<UserPaymentDto> findAll(PageRequest pageRequest) {
        return payRepository.findAll(pageRequest).stream().map(this::entityToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserPaymentDto> findById(Long id) {
        return payRepository.findById(id)
                .map(pay -> {
                    pay.getBuyer().getQuestions().size();
                    pay.getBuyer().getIssues().size();
                    pay.getProduct().getPayments().size();

                    return entityToDto(pay);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserPaymentDto> findByLawyer(String lawyer) {
        return payRepository.findByLawyer(lawyer)
                .map(pay -> {
                    pay.getBuyer().getQuestions().size();
                    pay.getBuyer().getIssues().size();
                    pay.getProduct().getPayments().size();

                    return entityToDto(pay);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserPaymentDto> findByBuyerId(Long buyerId) {
        List<UserPayment> payments = payRepository.findByBuyerId(buyerId);

        return payments.stream()
                .map(pay -> {
                    pay.getBuyer().getQuestions().size();
                    pay.getBuyer().getIssues().size();
                    pay.getProduct().getPayments().size();
                    return entityToDto(pay);
                })
                .toList();
    }


    @Override
    public Messenger count() {
        return Messenger.builder()
                .message(payRepository.count() + "").build();
    }

    @Override
    public boolean existsById(Long id) {
        return payRepository.existsById(id);
    }

    @Transactional
    @Override
    public Messenger update(UserPaymentDto dto) {
        Optional<UserPayment> payment = payRepository.findById(dto.getId());
        if (payment.isPresent()) {
            UserPayment pay = payment.get();
            pay.setStatus(dto.getStatus());
            pay.setBuyer(dto.getBuyer());
            pay.setProduct(dto.getProduct());
            pay.setAmount(dto.getAmount());
            pay.setLawyer(dto.getLawyer());
            payRepository.save(pay);
            return Messenger.builder().message("SUCCESS").build();
        }
        return Messenger.builder()
                .message("FAILURE")
                .build();
    }


}