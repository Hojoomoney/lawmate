package site.lawmate.manage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.lawmate.manage.domain.model.UserStats;

@Repository
public interface ManageRepository extends JpaRepository<UserStats,Long>, ManageDao{
}
