package site.lawmate.user.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import site.lawmate.user.component.Messenger;
import site.lawmate.user.domain.dto.IssueDto;
import site.lawmate.user.domain.model.Issue;

import java.util.List;

public interface IssueService extends CommandService<IssueDto>, QueryService<IssueDto> {
    default Issue dtoToEntity(IssueDto dto) {
        return Issue.builder()
                .law(dto.getLaw())
                .title(dto.getTitle())
                .content(dto.getContent())
                .date(dto.getDate())
                .time(dto.getTime())
                .lawyer(dto.getLawyer())
                .build();
    }

    default IssueDto entityToDto(Issue issue) {
        return IssueDto.builder()
                .id(issue.getId())
                .law(issue.getLaw())
                .title(issue.getTitle())
                .content(issue.getContent())
                .date(issue.getDate())
                .time(issue.getTime())
                .lawyer(issue.getLawyer())
                .build();
    }

    Messenger update(IssueDto dto);

    Messenger count();

    void addEmitter(SseEmitter emitter);

    List<IssueDto> getAllNotifications(String lawyer);
}
