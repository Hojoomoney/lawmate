package site.lawmate.lawyer.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import site.lawmate.lawyer.domain.model.File;

@Repository
public interface FileRepository extends ReactiveMongoRepository<File, String> {
    Flux<File> findAllByPostId(String id);

    Flux<File> findAllByLawyerId(String lawyerId);
}
