package site.lawmate.manage.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.lawmate.manage.domain.dto.UserStatsDto;
import site.lawmate.manage.domain.model.UserStats;
import site.lawmate.manage.repository.ManageMapper;
import site.lawmate.manage.repository.ManageRepository;
import site.lawmate.manage.service.ManageService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ManageServiceImpl implements ManageService {

    private final ManageRepository manageRepository;
    private final ManageMapper manageMapper;
    private final LocalDate yesterday = LocalDate.now().minusDays(1);
    @Override
    public void saveUserStats() {
        manageRepository.save(UserStats.builder()
                .date(LocalDate.now().minusDays(1))
                .newUserCount(selectYesterdayNewUserCount())
                .increaseRate(getIncreaseRate())
                .build());
    }

    @Override
    public List<UserStatsDto> findByDate() {
        return manageRepository.findAll().stream().map(i -> UserStatsDto.builder()
                .date(i.getDate())
                .newUserCount(i.getNewUserCount())
                .increaseRate(i.getIncreaseRate())
                .build()
        ).toList();
    }

    @Override
    public List<UserStatsDto> findByMonth() {
        return manageRepository.getUserStatsByMonth();
    }
    @Override
    public List<UserStatsDto> findByYear() {
        return manageRepository.getUserStatsByYear();
    }

    @Override
    public Map<String, Long> getUserTotalStats() {
        Map<String, Long> result = new HashMap<>();
        List<Map<String, Long>> ageGroupList = manageMapper.selectUserCountByAgeGroup();

        ageGroupList.forEach(tuple -> {
            String ageGroup = String.valueOf(tuple.get("ageGroup"));
            Long count = tuple.get("count");
            result.put(ageGroup, count);
        });
        result.put("total", selectTotalUserCount());
        result.put("male", manageMapper.selectCountMale());
        result.put("female", manageMapper.selectCountFemale());
        return result;
    }

    @Override
    public Long getIncreaseRate() {
        return (Math.round(selectTotalUserCount() / (double) (selectTotalUserCount() - selectYesterdayNewUserCount()) * 100) - 100);
    }

    @Override
    public Long selectTotalUserCount() {
        return manageMapper.selectCountUsers();
    }

    @Override
    public Long selectYesterdayNewUserCount() {
        return manageMapper.selectYesterdayNewUserCount(yesterday.atStartOfDay(), yesterday.plusDays(1).atStartOfDay());
    }

}
