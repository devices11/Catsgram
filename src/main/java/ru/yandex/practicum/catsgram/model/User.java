package ru.yandex.practicum.catsgram.model;

import lombok.*;

import java.time.Instant;

@Data
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Instant registrationDate;
}
