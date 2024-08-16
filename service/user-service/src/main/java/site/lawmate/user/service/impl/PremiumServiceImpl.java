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

        Premium savedPremium = premiumRepository.save(premium);

        return Messenger.builder()
                .message(premiumRepository.existsById(savedPremium.getId()) ? "SUCCESS" : "FAILURE")
                .build();
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
            premium.setPrice(premiumDto.getPrice());
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

}
