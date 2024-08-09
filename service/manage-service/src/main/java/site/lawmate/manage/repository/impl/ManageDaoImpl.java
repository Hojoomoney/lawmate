package site.lawmate.manage.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.lawmate.manage.domain.dto.UserStatsDto;
import site.lawmate.manage.domain.model.QUserStats;
import site.lawmate.manage.repository.ManageDao;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class ManageDaoImpl implements ManageDao {

    private final JPAQueryFactory factory;
    private final QUserStats userStats = QUserStats.userStats;

    @Override
    public List<UserStatsDto> getUserStatsByMonth() {
        return factory.select(
                        Projections.fields(UserStatsDto.class,
                                userStats.date.yearMonth().as("month"),
                                userStats.newUserCount.sum().as("newUserCount"),
                                userStats.increaseRate.avg().round().longValue().as("increaseRateAverage")
                        ))
                .from(userStats)
                .groupBy(userStats.date.yearMonth())
                .orderBy(userStats.date.yearMonth().desc())
                .fetch();
    }

    @Override
    public List<UserStatsDto> getUserStatsByYear() {
        return factory.select(
                        Projections.fields(UserStatsDto.class,
                                userStats.date.year().as("year"),
                                userStats.newUserCount.sum().as("newUserCount"),
                                userStats.increaseRate.avg().round().longValue().as("increaseRateAverage")
                        ))
                .from(userStats)
                .groupBy(userStats.date.year())
                .orderBy(userStats.date.year().desc())
                .fetch();
    }

}
