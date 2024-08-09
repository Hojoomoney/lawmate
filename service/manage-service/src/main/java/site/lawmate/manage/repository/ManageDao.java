package site.lawmate.manage.repository;

import site.lawmate.manage.domain.dto.UserStatsDto;

import java.util.List;

public interface ManageDao{

    List<UserStatsDto> getUserStatsByMonth();
    List<UserStatsDto> getUserStatsByYear();

}
