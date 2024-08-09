package site.lawmate.lawyer.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import site.lawmate.lawyer.domain.model.Post;

@Repository
public interface PostRepository extends ReactiveMongoRepository<Post, String> {
    Flux<Post> findAllByLawyerId(String lawyerId);
    Flux<Post> findAllBy(PageRequest pageRequest);
}
