package site.lawmate.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.lawmate.user.domain.model.UserPayment;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPaymentRepository extends JpaRepository<UserPayment, Long> {
    List<UserPayment> findByImpUid(String impUid);

    @Query("SELECT p FROM user_payments p " +
            "JOIN FETCH p.buyer b " +
            "JOIN FETCH p.product pr " +
            "WHERE b.id = :buyerId")
    List<UserPayment> findByBuyerId(@Param("buyerId") Long buyerId);
    Optional<UserPayment> findByLawyer(String lawyer);

}