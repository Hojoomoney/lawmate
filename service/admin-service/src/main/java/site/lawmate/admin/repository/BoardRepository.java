package site.lawmate.admin.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import site.lawmate.admin.domain.model.Board;

@Repository
public interface BoardRepository extends ReactiveMongoRepository<Board,String> {
    Flux<Board> findAllBy(PageRequest pageRequest);
}
