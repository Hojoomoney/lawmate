package site.lawmate.user.service;

import org.springframework.data.domain.PageRequest;
import site.lawmate.user.component.Messenger;

import java.util.List;
import java.util.Optional;

public interface QueryService<T> {
    List<T> findAll(PageRequest pageRequest);

    Optional<T> findById(Long id);

    Messenger count();

    boolean existsById(Long id);
}
