package site.lawmate.lawyer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import site.lawmate.lawyer.domain.model.Reservation;
import site.lawmate.lawyer.repository.ReservationRepository;
import site.lawmate.lawyer.service.ReservationService;


@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final Sinks.Many<Reservation> sink = Sinks.many().multicast().onBackpressureBuffer();

//    public Mono<Reservation> createReservation(Reservation res) {
//        return reservationRepository.save(res);
//    }

    public Mono<Reservation> updateReservationStatus(String id, Reservation res) {
        return reservationRepository.findById(id)
                .flatMap(reservation -> {
                    reservation.setStatus(res.getStatus());
                    return reservationRepository.save(reservation);
                });
    }

    public Flux<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Flux<Reservation> getReservationByLawyerId(String lawyerId) {
        return reservationRepository.findByLawyerId(lawyerId);
    }

    public Mono<Void> deleteReservation(String id) {
        return reservationRepository.deleteById(id);
    }

    public Mono<Void> deleteAllReservations() {
        return reservationRepository.deleteAll();
    }
    public Mono<Reservation> createReservation(Reservation reservation) {
        reservation.setStatus("수락 대기 중..");
        return reservationRepository.save(reservation)
                .doOnSuccess(sink::tryEmitNext);
    }

    public Mono<Reservation> updateReservationStatus(String id, String status) {
        return reservationRepository.findById(id)
                .flatMap(reservation -> {
                    reservation.setStatus(status);
                    return reservationRepository.save(reservation)
                            .doOnSuccess(sink::tryEmitNext);
                });
    }

    public Flux<Reservation> getReservationUpdates() {
        return sink.asFlux();
    }
}
