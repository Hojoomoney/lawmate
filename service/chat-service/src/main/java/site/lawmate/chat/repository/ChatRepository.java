package site.lawmate.chat.repository;

import io.micrometer.observation.ObservationFilter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.lawmate.chat.domain.model.Chat;

import java.util.List;

@Repository
public interface ChatRepository extends ReactiveMongoRepository<Chat, ObjectId> {
    @Query("{ 'userId': ?0, 'chattingRoomId': ?1 }")
    Flux<Chat> findByUserIdAndChattingRoomId(Long userId, Long chattingRoomId);
    Mono<Void> deleteByUserIdAndChattingRoomId(Long userId, Long chattingRoomId);

    @Query("{ 'userId': ?0 }")
    Flux<Chat> findByUserId(Long userId);

    Flux<Chat> findAllByUserId(Long userId);

    Flux<Chat> findByUserIdAndChattingRoomIdOrderByMessageOrderAsc(Long userId, Long chattingRoomId);
    Flux<Chat> findByUserIdAndChattingRoomIdOrderByChatDateAsc(Long userId, Long chattingRoomId);



}
