package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) throws ConditionsNotMetException {
        if (user.getEmail().isEmpty() || user.getEmail().isBlank())
            throw new ConditionsNotMetException("Имейл должен быть указан");
        if (users.containsKey(user.getId()))
            throw new DuplicatedDataException("Этот имейл уже используется");
        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) throws ConditionsNotMetException {
        if (user.getId() == null)
            throw new ConditionsNotMetException("Id должен быть указан");

        if (user.getEmail() != null
                && user.getEmail().equals(users.get(user.getId()).getEmail())
                && !Objects.equals(user.getId(), users.get(user.getId()).getId()))
            throw new DuplicatedDataException("Этот имейл уже используется");

        if (user.getEmail() == null)
            user.setEmail(users.get(user.getId()).getEmail());
        else if (user.getName() == null) {
            user.setName(users.get(user.getId()).getName());
        } else if (user.getPassword() == null) {
            user.setPassword(users.get(user.getId()).getPassword());
        }

        user.setRegistrationDate(users.get(user.getId()).getRegistrationDate());
        users.put(user.getId(), user);

        return users.get(user.getId());
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
