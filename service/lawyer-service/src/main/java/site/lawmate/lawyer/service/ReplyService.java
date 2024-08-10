package site.lawmate.lawyer.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.lawmate.lawyer.domain.model.Lawyer;
import site.lawmate.lawyer.domain.model.Reply;

public interface ReplyService {
    Mono<Lawyer> replyToLawyer(String id, String articleId, Reply reply);
    Mono<Reply> updateReply(String id, Reply replyModel);
    Mono<Reply> getReplyByArticleId(String articleId);
    Flux<Reply> getAllReplies();
    Mono<Void> deleteReply(String id);
    Flux<Reply> getRepliesByLawyerId(String lawyerId);
    Mono<Void> deleteAllReplies();

}
