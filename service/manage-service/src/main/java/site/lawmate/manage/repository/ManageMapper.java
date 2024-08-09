package site.lawmate.manage.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface ManageMapper {
    Long selectCountUsers();
    Long selectCountMale();
    Long selectCountFemale();

    List<Map<String, Long>> selectUserCountByAgeGroup();

    Long selectYesterdayNewUserCount(@Param("yesterdayStart") LocalDateTime yesterdayStart, @Param("todayStart") LocalDateTime todayStart);
}
