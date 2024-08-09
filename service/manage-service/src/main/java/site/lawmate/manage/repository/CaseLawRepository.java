package site.lawmate.manage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.lawmate.manage.domain.model.CaseLaw;

@Repository
public interface CaseLawRepository extends JpaRepository<CaseLaw, String>, CaseLawDao {
}
