package site.lawmate.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import site.lawmate.user.domain.model.LawPayment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LawPaymentRepository extends JpaRepository<LawPayment, Long> {
    LawPayment findByImpUid(String impUid);

    List<LawPayment> findByExpireDateBeforeAndIsExpiredFalse(LocalDate date);

    @Transactional
    @Modifying
    @Query("UPDATE law_payments p SET p.isExpired = true WHERE p.id = :id")
    void markAsExpired(Long id);

    Optional<LawPayment> findByLawyer(String lawyer);
}
