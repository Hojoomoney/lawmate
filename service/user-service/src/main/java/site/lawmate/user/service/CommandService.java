package site.lawmate.user.service;

import site.lawmate.user.component.Messenger;

public interface CommandService<T> {
    Messenger save(T t);

    Messenger delete(Long id);

    Messenger update(T t);
}
