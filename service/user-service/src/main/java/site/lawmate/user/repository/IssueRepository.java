package site.lawmate.user.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.lawmate.user.domain.model.Issue;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long>{

    List<Issue> findAllByOrderByIdDesc(PageRequest pageRequest);

    List<Issue> findAllByLawyer(String lawyer);
}
