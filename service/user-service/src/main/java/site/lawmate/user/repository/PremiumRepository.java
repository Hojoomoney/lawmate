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
    List<Premium> findByExpireDateBeforeAndIsExpiredFalse(LocalDate date);

    @Transactional
    @Modifying
    @Query("UPDATE premiums p SET p.isExpired = true WHERE p.id = :id")
    void markAsExpired(Long id);

    List<Premium> findAllByOrderByIdAsc();

    Optional<Premium> findByLawyer(String lawyer);

}
