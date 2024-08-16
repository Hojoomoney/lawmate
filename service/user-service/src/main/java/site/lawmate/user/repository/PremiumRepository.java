package site.lawmate.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import site.lawmate.user.domain.model.Premium;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PremiumRepository extends JpaRepository<Premium, Long> {

    List<Premium> findAllByOrderByIdAsc();

}
