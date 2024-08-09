package site.lawmate.user.service;

import site.lawmate.user.domain.dto.PremiumDto;
import site.lawmate.user.domain.model.Premium;

import java.util.Optional;

public interface PremiumService extends CommandService<PremiumDto>, QueryService<PremiumDto> {

    default Premium dtoToEntity(PremiumDto dto) {
        return Premium.builder()
                .startDate(dto.getStartDate())
                .expireDate(dto.getExpireDate())
                .plan(dto.getPlan())
                .price(dto.getPrice())
                .build();
    }

    default PremiumDto entityToDto(Premium premium) {
        return PremiumDto.builder()
                .id(premium.getId())
                .startDate(premium.getStartDate())
                .expireDate(premium.getExpireDate())
                .plan(premium.getPlan())
                .price(premium.getPrice())
                .build();
    }

    Optional<PremiumDto> findByLawyer(String lawyer);

}
