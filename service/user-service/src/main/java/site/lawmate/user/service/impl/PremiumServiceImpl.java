package site.lawmate.user.service.impl;

import com.siot.IamportRestClient.IamportClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.lawmate.user.component.Messenger;
import site.lawmate.user.domain.dto.PremiumDto;
import site.lawmate.user.domain.model.Premium;
import site.lawmate.user.repository.PremiumRepository;
import site.lawmate.user.service.PremiumService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PremiumServiceImpl implements PremiumService {
    private final PremiumRepository premiumRepository;
    private final IamportClient iamportClient;

    @Override
    public Messenger save(PremiumDto dto) {
        Premium premium = dtoToEntity(dto);
        premium.setStartDate(LocalDateTime.now());
        premium.setExpireDate(calculateExpireDate(premium.getPlan(), premium.getStartDate()));

        Premium savedPremium = premiumRepository.save(premium);

        return Messenger.builder()
                .message(premiumRepository.existsById(savedPremium.getId()) ? "SUCCESS" : "FAILURE")
                .build();
    }

    private LocalDateTime calculateExpireDate(String plan, LocalDateTime startDate) {
        return switch (plan.toLowerCase()) {
            case "monthly" -> startDate.plusMonths(1);
            case "semi-annual" -> startDate.plusMonths(6);
            case "annual" -> startDate.plusYears(1);
            default -> throw new IllegalArgumentException("Invalid plan: " + plan);
        };
    }


    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void checkAndExpirePremiums() {
        List<Premium> expiredPremiums = premiumRepository.findByExpireDateBeforeAndIsExpiredFalse(LocalDate.now());
        for (Premium premium : expiredPremiums) {
            premiumRepository.markAsExpired(premium.getId());
        }
    }

    @Override
    public Messenger delete(Long id) {
        premiumRepository.deleteById(id);
        return Messenger.builder()
                .message(premiumRepository.existsById(id) ? "FAILURE" : "SUCCESS")
                .build();
    }

    @Override
    @Transactional
    public Messenger update(PremiumDto premiumDto) {
        Optional<Premium> optionalPremium = premiumRepository.findById(premiumDto.getId());
        if (optionalPremium.isPresent()) {
            Premium premium = optionalPremium.get();
            premium.setPlan(premiumDto.getPlan());
            premium.setPrice(premiumDto.getPrice());
            premium.setLawyer(premiumDto.getLawyer());
            premiumRepository.save(premium);
            return Messenger.builder().message("SUCCESS").build();
        }
        return Messenger.builder().message("FAILURE").build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PremiumDto> findAll(PageRequest pageRequest) {
        return premiumRepository.findAll(pageRequest).stream()
                .map(this::entityToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PremiumDto> findById(Long id) {
        return premiumRepository.findById(id).map(this::entityToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Messenger count() {
        long count = premiumRepository.count();
        return Messenger.builder().message(String.valueOf(count)).build();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return premiumRepository.existsById(id);
    }

    @Override
    public Optional<PremiumDto> findByLawyer(String lawyer) {
        return premiumRepository.findByLawyer(lawyer)
                .map(this::entityToDto);    }
}
