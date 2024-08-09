package site.lawmate.manage.service;

import site.lawmate.manage.domain.dto.UserStatsDto;

import java.util.List;
import java.util.Map;

public interface ManageService {
    void saveUserStats();

    List<UserStatsDto> findByDate();

    List<UserStatsDto> findByMonth();
    List<UserStatsDto> findByYear();

    Map<String, Long> getUserTotalStats();

    Long getIncreaseRate();

    Long selectTotalUserCount();
    Long selectYesterdayNewUserCount();

}
