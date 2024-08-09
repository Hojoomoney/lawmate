package site.lawmate.lawyer.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import site.lawmate.lawyer.domain.model.Notice;

@Repository
public interface NoticeRepository extends ReactiveMongoRepository<Notice, String> {
    Flux<Notice> findByUserId(String userId);
}
