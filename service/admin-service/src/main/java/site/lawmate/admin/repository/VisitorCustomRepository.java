package site.lawmate.admin.repository;

import reactor.core.publisher.Mono;

public interface VisitorCustomRepository {


    Mono<Long> incrementVisitorCount(String key);

    Mono<String> getVisitorCount(String key);

    Mono<Long> getVisitorCountByDate(String date);

    Mono<Long> getVisitorCountByMonth(String year, String month);

}
