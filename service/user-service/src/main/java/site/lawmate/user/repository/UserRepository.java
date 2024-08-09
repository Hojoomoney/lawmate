package site.lawmate.user.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.lawmate.user.domain.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByOrderByIdDesc(PageRequest pageRequest);

    Optional<User> findByEmail(String email);

    @Query("select count(id) as count from users where email =:email")
    Integer existsByEmail(@Param("email") String username);

    @Modifying
    @Query("update users a set a.point = a.point + :point where a.id = :id")
    void plusPoint(@Param("id") Long id, @Param("point") Long point);

    @Modifying
    @Query("update users a set a.point = a.point - :point where a.id = :id")
    void minusPoint(@Param("id") Long id, @Param("point") Long point);

}
