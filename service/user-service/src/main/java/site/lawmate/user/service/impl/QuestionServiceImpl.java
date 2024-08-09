package site.lawmate.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.lawmate.user.component.Messenger;
import site.lawmate.user.domain.dto.QuestionDto;
import site.lawmate.user.domain.model.Question;
import site.lawmate.user.domain.model.User;
import site.lawmate.user.repository.QuestionRepository;
import site.lawmate.user.repository.UserRepository;
import site.lawmate.user.service.QuestionService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@Service
@RequiredArgsConstructor
@RequestMapping(path = "/questions")
@Slf4j
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public Messenger save(QuestionDto dto) {
        log.info("Parameters received through save service: " + dto);
        User writer = userRepository.findById(dto.getWriter().getId())
                .orElseThrow(() -> new IllegalArgumentException("User " + dto.getWriter() + " not found."));
        Question question = Question.builder()
                .law(dto.getLaw())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(writer)
                .build();
        Question savedQuestion = questionRepository.save(question);
        return Messenger.builder()
                .message(questionRepository.existsById(savedQuestion.getId()) ? "SUCCESS" : "FAILURE")
                .build();
    }

    @Transactional
    @Override
    public Messenger delete(Long id) {
        questionRepository.deleteById(id);
        return Messenger.builder()
                .message(
                        Stream.of(id)
                                .filter(i -> existsById(i))
                                .peek(i -> questionRepository.deleteById(i))
                                .map(i -> "FAILURE")
                                .findAny()
                                .orElseGet(() -> "SUCCESS")
                )
                .build();
    }

    @Transactional
    @Override
    public Messenger update(QuestionDto dto) {
        Optional<Question> optionalQuestion = questionRepository.findById(dto.getId());
        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            Question updateQuestion = question.toBuilder()
                    .law(dto.getLaw())
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .build();
            Long updateQuestionId = questionRepository.save(updateQuestion).getId();

            return Messenger.builder()
                    .message("SUCCESS ID is " + updateQuestionId)
                    .build();
        } else {
            return Messenger.builder()
                    .message("FAILURE").build();
        }
    }

    @Transactional
    public List<QuestionDto> findAll(PageRequest pageRequest) {
        return questionRepository.findAllByOrderByIdDesc(pageRequest)
                .stream()
                .map(this::entityToDto)
                .toList();
    }

    @Override
    public Optional<QuestionDto> findById(Long id) {
        return questionRepository.findById(id).map(this::entityToDto);
    }

    @Override
    public Messenger count() {
        return Messenger.builder()
                .message(questionRepository.count() + "")
                .build();
    }

    @Override
    public boolean existsById(Long id) {
        return questionRepository.existsById(id);
    }

    @Override
    public List<QuestionDto> findByTitleAndContent(String keyword) {
        Specification<Question> spec = (root, query, criteriaBuilder) -> {
            String likePattern = "%" + keyword + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(root.get("title"), likePattern),
                    criteriaBuilder.like(root.get("content"), likePattern)
            );
        };

        return questionRepository.findAll(spec).stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Question dtoToEntity(QuestionDto dto) {
        return Question.builder()
                .law(dto.getLaw())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
    }

    @Override
    public QuestionDto entityToDto(Question question) {
        return QuestionDto.builder()
                .id(question.getId())
                .law(question.getLaw())
                .title(question.getTitle())
                .content(question.getContent())
                .writer(question.getWriter())
                .build();
    }
}
