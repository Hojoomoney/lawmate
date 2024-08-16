package site.lawmate.lawyer.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public interface NoticeService {
    void sendNotification(String userId, String message);

    Flux<String> getNotifications(String userId);

    void removeUserSink(String userId);
}
