package site.lawmate.admin.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import site.lawmate.admin.repository.VisitorCustomRepository;

@Repository
@RequiredArgsConstructor
public class VisitorCustomRepositoryImpl implements VisitorCustomRepository {
    private final ReactiveStringRedisTemplate redisTemplate;

    @Override
    public Mono<Long> incrementVisitorCount(String key) {
        return redisTemplate.opsForValue().increment(key);
    }
    @Override
    public Mono<String> getVisitorCount(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    @Override
    public Mono<Long> getVisitorCountByDate(String date) {
        return redisTemplate.opsForValue().get("visitorCount:" + date)
                .map(Long::parseLong);
    }

    @Override
    public Mono<Long> getVisitorCountByMonth(String year, String month) {
        return redisTemplate.keys("visitorCount:" + year + "-" + month + "*")
                .flatMap(redisTemplate.opsForValue()::get)
                .map(Long::parseLong)
                .reduce(0L, Long::sum); // reduce()는 Flux 에서 방출된 모든 값을 하나로 합침 (0L은 초기값)
    }
}
