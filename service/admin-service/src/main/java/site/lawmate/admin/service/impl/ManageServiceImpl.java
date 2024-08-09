package site.lawmate.admin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.lawmate.admin.domain.dto.LawyerStatsDto;
import site.lawmate.admin.domain.model.Lawyer;
import site.lawmate.admin.domain.model.LawyerStats;
import site.lawmate.admin.repository.LawyerStatsRepository;
import site.lawmate.admin.repository.ManageRepository;
import site.lawmate.admin.service.ManageService;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Service
public class ManageServiceImpl implements ManageService {

    private final ManageRepository manageRepository;
    private final LawyerStatsRepository lawyerStatsRepository;
    private final LocalDate yesterday = LocalDate.now().minusDays(1);
    @Override
    public Mono<LawyerStatsDto> countLawyersAll() {

        return Mono.zip(countLawyers(), countLawyersAuthFalse(), countLawyersAuthTrue())
                .map(tuple -> LawyerStatsDto.builder()
                        .totalLawyers(tuple.getT1())
                        .totalLawyersAuthFalse(tuple.getT2())
                        .totalLawyersAuthTrue(tuple.getT3())
                        .build());
    }

    @Override
    public Flux<Lawyer> getLawyersAuthFalse() {
        return manageRepository.findAll().filter(lawyer -> !lawyer.getAuth());
    }

    @Override
    public Mono<Long> countLawyersAuthFalse() {
        return manageRepository.findAll().filter(lawyer -> !lawyer.getAuth()).count();
    }

    @Override
    public Mono<Long> countLawyersAuthTrue() {
        return manageRepository.findAll().filter(Lawyer::getAuth).count();
    }

    @Override
    public Mono<Long> countLawyers() {
        return manageRepository.count();
    }

    @Override
    public Mono<Long> countNewLawyers() {
        return manageRepository.findAll().filter(lawyer -> lawyer.getCreatedDate().toLocalDate().equals(yesterday)).count();
    }

    @Override
    public Mono<Long> getIncreaseRate() {
     return countLawyers()
             .flatMap(total -> countNewLawyers()
                     .map(newLawyers -> Math.round(total / (double) (total - newLawyers) * 100) - 100));
    }

    @Override
    public void saveLawyerStats() {
        log.info("saveLawyerStats");
       Mono.zip(countNewLawyers(), getIncreaseRate(), countLawyersAuthFalse())
               .map(tuple -> LawyerStats.builder()
                       .date(yesterday)
                       .newLawyerCount(tuple.getT1())
                       .increaseRate(tuple.getT2())
                       .countLawyersFalse(tuple.getT3())
                       .build())
               .flatMap(lawyerStatsRepository::save)
               .subscribe();
    }

    @Override
    public Flux<LawyerStatsDto> getLawyerStats() {
        return lawyerStatsRepository.findAll().map(lawyerStats -> LawyerStatsDto.builder()
                .date(lawyerStats.getDate())
                .newLawyerCount(lawyerStats.getNewLawyerCount())
                .increaseRate(lawyerStats.getIncreaseRate())
                .totalLawyersAuthFalse(lawyerStats.getCountLawyersFalse())
                .build());
    }

    @Override
    public Mono<String> setLawyerAuthTrue(String id) {
        return manageRepository.findById(id)
                .map(lawyer -> {
                    lawyer.setAuth(true);
                    return lawyer;
                })
                .flatMap(manageRepository::save)
                .flatMap(lawyer -> Mono.just("Auth Success"))
                .switchIfEmpty(Mono.just("Auth Failure"));
    }


}
