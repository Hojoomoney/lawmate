package site.lawmate.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.lawmate.user.domain.model.UserPayment;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPaymentRepository extends JpaRepository<UserPayment, Long> {
    List<UserPayment> findByImpUid(String impUid);

    @Query("SELECT p FROM user_payments p WHERE p.buyer.id = :buyerId")
    List<UserPayment> findByBuyerId(Long buyerId);

    Optional<UserPayment> findByLawyer(String lawyer);

}