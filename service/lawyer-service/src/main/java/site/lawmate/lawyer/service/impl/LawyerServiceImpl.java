    package site.lawmate.lawyer.service.impl;
    
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.data.domain.Sort;
    import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
    import org.springframework.data.mongodb.core.query.Criteria;
    import org.springframework.data.mongodb.core.query.Query;
    import org.springframework.http.codec.multipart.FilePart;
    import org.springframework.stereotype.Service;
    import reactor.core.publisher.Flux;
    import reactor.core.publisher.Mono;
    import site.lawmate.lawyer.component.Messenger;
    import site.lawmate.lawyer.domain.dto.LawyerDto;
    import site.lawmate.lawyer.domain.model.LawyerDetail;
    import site.lawmate.lawyer.domain.model.Lawyer;
    import site.lawmate.lawyer.repository.LawyerDetailRepository;
    import site.lawmate.lawyer.repository.LawyerRepository;
    import site.lawmate.lawyer.service.LawyerService;
    
    import java.util.List;
    import java.util.UUID;
    
    @Service
    @Slf4j
    @RequiredArgsConstructor
    public class LawyerServiceImpl implements LawyerService {
    
        private final LawyerRepository lawyerRepository;
        private final LawyerDetailRepository lawyerDetailRepository;
        private final ReactiveMongoTemplate reactiveMongoTemplate;
        private final EmailServiceImpl emailService;
        private final S3ServiceImpl s3Service;
    
        @Override
        public Flux<Lawyer> getAllLawyers() {
            return lawyerRepository.findAll();
        }
        @Override
        public Mono<Long> getLawyersCount() {
            return lawyerRepository.count();
        }
    
        @Override
        public Mono<Lawyer> getLawyerById(String id) {
            return lawyerRepository.findById(id);
        }
    
    
        // 변호사 추가 정보
        // 변호사 상세 정보를 추가하는 메서드
        public Mono<Lawyer> addLawyerDetailToLawyer(String id, LawyerDetail detail, FilePart photoFile) {
            return s3Service.uploadFile(photoFile)
                    .flatMap(photoUrl -> {
                        detail.setPhoto(photoUrl); // 업로드된 사진 URL을 설정
                        return lawyerDetailRepository.save(detail);
                    })
                    .flatMap(savedDetail -> lawyerRepository.findById(id)
                            .flatMap(lawyer -> {
                                lawyer.setDetail(savedDetail);
                                return lawyerRepository.save(lawyer);
                            }));
        }
        @Override
        public Mono<LawyerDetail> getLawyerDetailById(String id) {
            return lawyerRepository.findById(id)
                    .map(Lawyer::getDetail)
                    ;
        }
        @Override
        public Mono<Lawyer> addLawyer(Lawyer lawyer) {
            return lawyerRepository.findByEmail(lawyer.getEmail())
                    .flatMap(existingLawyer -> {
                        return Mono.<Lawyer>error(new IllegalArgumentException("이미 가입된 이메일입니다."));
                    })
                    .switchIfEmpty(lawyerRepository.save(lawyer));
        }
        @Override
        public Mono<Lawyer> updateLawyer(String id, Lawyer lawyer) {
            return lawyerRepository.findById(id)
                    .flatMap(optionalLawyer -> {
                        if (lawyer.getPassword() != null) {
                            optionalLawyer.setPassword(lawyer.getPassword());
                        }
                        if (lawyer.getMid() != null) {
                            optionalLawyer.setMid(lawyer.getMid());
                        }
                        if (lawyer.getPhone() != null) {
                            optionalLawyer.setPhone(lawyer.getPhone());
                        }
                        if (lawyer.getAuth() != null){
                            optionalLawyer.setAuth(lawyer.getAuth());
                        }
                        return lawyerRepository.save(optionalLawyer);
                    })
                    .switchIfEmpty(Mono.empty());
        }
        @Override
        public Mono<Void> deleteLawyer(String id) {
            return lawyerRepository.deleteById(id);
        }

        @Override
        public Mono<Lawyer> updateLawyerDetail(String id, LawyerDetail detail, FilePart photoFile) {
            return s3Service.uploadFile(photoFile)
                    .flatMap(photoUrl -> {
                        detail.setPhoto(photoUrl); // 업로드된 사진 URL을 설정
                        return lawyerDetailRepository.save(detail);
                    })
                    .flatMap(savedDetail -> lawyerRepository.findById(id)
                            .flatMap(lawyer -> {
                                lawyer.setDetail(savedDetail);
                                return lawyerRepository.save(lawyer);
                            }));
        }
        @Override
        public Flux<Lawyer> getLawyersByLaw(List<String> law) {
            Query query = new Query();
            query.addCriteria(Criteria.where("detail.law").in(law).and("auth").is(true));
            query.with(Sort.by(Sort.Order.desc("detail.premium")));
    
            return reactiveMongoTemplate.find(query, Lawyer.class);
        }
        @Override
        public Flux<Lawyer> getLawyersBySearch(String search) {
            Query query = new Query();
            Criteria criteria = new Criteria();
            String regex = ".*" + search + ".*";
            criteria.orOperator(
                    Criteria.where("detail.law").regex(regex),
                    Criteria.where("name").regex(regex),
                    Criteria.where("detail.belong").regex(regex),
                    Criteria.where("detail.address").regex(regex)
            );
            criteria.andOperator(Criteria.where("auth").is(true));
            query.addCriteria(criteria);
            query.with(Sort.by(Sort.Order.desc("detail.premium")));
    
            return reactiveMongoTemplate.find(query, Lawyer.class);
        }
    
        @Override
        public Mono<Void> resetPassword(String lawyerNo) {
            return lawyerRepository.findByLawyerNo(lawyerNo)
                    .flatMap(lawyer -> {
                        String newPassword = UUID.randomUUID().toString().substring(0, 8);  // 새로운 비밀번호 생성
                        lawyer.setPassword(newPassword);  // 비밀번호 업데이트
                        return lawyerRepository.save(lawyer)
                                .then(emailService.sendResetPasswordEmail(lawyer.getEmail(), newPassword));
                    });
        }
    }