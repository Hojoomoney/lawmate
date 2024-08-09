package site.lawmate.lawyer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import site.lawmate.lawyer.service.NoticeService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final Map<String, Sinks.Many<String>> userSinks = new ConcurrentHashMap<>();

    public void sendNotification(String userId, String message) {
        Sinks.Many<String> sink = userSinks.get(userId);
        if (sink != null) {
            sink.tryEmitNext(message);
        }
    }

    public Flux<String> getNotifications(String userId) {
        return userSinks.computeIfAbsent(userId, key -> Sinks.many().multicast().onBackpressureBuffer()).asFlux();
    }

    public void removeUserSink(String userId) {
        userSinks.remove(userId);
    }
}
