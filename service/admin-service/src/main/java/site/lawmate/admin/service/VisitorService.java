package site.lawmate.admin.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface VisitorService {

    Mono<Long> incrementVisitorCount();
    Mono<String> getVisitorCount();
    public Flux<String> getVisitorCountStream();
    public Mono<Map<String,Long>> getVisitorCountByLast7Days();
    public Mono<Long> getVisitorCountByMonth(String year, String month);
    public Mono<Map<String,Long>> getVisitorCountYearByMonth(String year);
}
