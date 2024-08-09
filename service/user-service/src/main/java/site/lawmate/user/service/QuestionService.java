package site.lawmate.user.service;

import site.lawmate.user.component.Messenger;
import site.lawmate.user.domain.dto.QuestionDto;
import site.lawmate.user.domain.model.Question;

import java.util.List;

public interface QuestionService extends CommandService<QuestionDto>, QueryService<QuestionDto> {

    default Question dtoToEntity(QuestionDto dto) {
        return Question.builder()
                .law(dto.getLaw())
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();
    }

    default QuestionDto entityToDto(Question question) {
        return QuestionDto.builder()
                .id(question.getId())
                .law(question.getLaw())
                .title(question.getTitle())
                .content(question.getContent())
                .writer(question.getWriter())
                .build();
    }

    Messenger update(QuestionDto dto);

    List<QuestionDto> findByTitleAndContent(String keyword);

}