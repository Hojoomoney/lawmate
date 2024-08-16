package site.lawmate.lawyer.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.lawmate.lawyer.domain.model.Reply;

@Repository
public interface ReplyRepository extends ReactiveMongoRepository<Reply, String>{

    Flux<Reply> findAllByLawyerId(String lawyerId);

    Flux<Reply> findAllByArticleId(String articleId);
}
