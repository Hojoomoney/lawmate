package site.lawmate.user.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.lawmate.user.domain.model.Question;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>, JpaSpecificationExecutor<Question> {

    @Query("select a from Question a where a.law =:law")
    List<Question> getQuestionsByLaw(String law);

    List<Question> findAllByOrderByIdDesc(PageRequest pageRequest);
}
