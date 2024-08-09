package site.lawmate.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import site.lawmate.user.component.Messenger;
import site.lawmate.user.domain.dto.IssueDto;
import site.lawmate.user.domain.model.Issue;
import site.lawmate.user.domain.model.User;
import site.lawmate.user.repository.IssueRepository;
import site.lawmate.user.repository.UserRepository;
import site.lawmate.user.service.IssueService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@Slf4j
@Service
@RequiredArgsConstructor
@RequestMapping(path = "/issues")
public class IssueServiceImpl implements IssueService {
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
    }

    @Override
    @Transactional(readOnly = true)
    public List<IssueDto> getAllNotifications(String lawyer) {
        log.info("issue 알림 service 진입 성공: {}", lawyer);
        return issueRepository.findAllByLawyer(lawyer)
                .stream()
                .map(this::entityToDto) // Issue 엔티티를 IssueDto로 변환
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Messenger save(IssueDto dto) {
        log.info("Parameters received through save service: " + dto);
        User client = userRepository.findById(dto.getClient().getId())
                .orElseThrow(() -> new IllegalArgumentException("User " + dto.getClient() + " not found."));
        Issue issue = Issue.builder()
                .law(dto.getLaw())
                .title(dto.getTitle())
                .content(dto.getContent())
                .client(dto.getClient())
                .lawyer(dto.getLawyer())
                .build();
        Issue savedIssue = issueRepository.save(issue);
        sendIssueUpdate(savedIssue);
        return Messenger.builder()
                .message(issueRepository.existsById(savedIssue.getId()) ? "SUCCESS" : "FAILURE")
                .build();
    }

    private void sendIssueUpdate(Issue issue) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(issue);
            } catch (IOException e) {
                emitter.complete();
                emitters.remove(emitter);
            }
        }
    }

    @Transactional
    @Override
    public Messenger delete(Long id) {
        issueRepository.deleteById(id);
        return Messenger.builder()
                .message(
                        Stream.of(id)
                                .filter(i -> existsById(i))
                                .peek(i -> issueRepository.existsById(i))
                                .map(i -> "FAILURE")
                                .findAny()
                                .orElseGet(() -> "SUCCESS")
                ).build();
    }

    @Transactional
    @Override
    public Messenger update(IssueDto dto) {
        Optional<Issue> optionalIssue = issueRepository.findById(dto.getId());
        if (optionalIssue.isPresent()) {
            Issue issue = optionalIssue.get();
            Issue updatedIssue = issue.toBuilder()
                    .law(dto.getLaw())
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .date(dto.getDate())
                    .time(dto.getTime())
                    .lawyer(dto.getLawyer())
                    .client(dto.getClient())
                    .build();
            Long upatedIssueId = issueRepository.save(updatedIssue).getId();

            return Messenger.builder()
                    .message("SUCCESS ID is " + upatedIssueId)
                    .build();
        } else {
            return Messenger.builder()
                    .message("FAILURE")
                    .build();
        }
    }

    @Transactional
    @Override
    public List<IssueDto> findAll(PageRequest pageRequest) {
        return issueRepository.findAllByOrderByIdDesc(pageRequest).stream().map(this::entityToDto).toList();
    }

    @Override
    public Optional<IssueDto> findById(Long id) {
        return issueRepository.findById(id).map(this::entityToDto);
    }

    @Override
    public Messenger count() {
        return Messenger.builder()
                .message(issueRepository.count() + "")
                .build();
    }

    @Override
    public boolean existsById(Long id) {
        return issueRepository.existsById(id);
    }
}
