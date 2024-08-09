package site.lawmate.lawyer.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.lawmate.lawyer.domain.model.Lawyer;
import site.lawmate.lawyer.domain.model.LawyerDetail;

import java.util.List;

public interface LawyerService {
    Flux<Lawyer> getAllLawyers();
    Mono<Long> getLawyersCount();
    Mono<Lawyer> getLawyerById(String id);
    Mono<Lawyer> addLawyerDetailToLawyer(String id, LawyerDetail detail);
    Mono<LawyerDetail> getLawyerDetailById(String id);
    Mono<Lawyer> addLawyer(Lawyer lawyer);
    Mono<Lawyer> updateLawyer(String id, Lawyer lawyer);
    Mono<Void> deleteLawyer(String id);
    Flux<Lawyer> findByName(String name);
    Mono<Lawyer> updateLawyerDetail(String id, LawyerDetail detail);
    Mono<Lawyer> getLawyerByEmail(String username);
    Mono<LawyerDetail> getLawyerDetailByEmail(String email);
    Flux<Lawyer> getLawyersByLaw(List<String> law);
    Flux<Lawyer> getLawyersBySearch(String search);
    Mono<Void> resetPassword(String lawyerNo);
}
