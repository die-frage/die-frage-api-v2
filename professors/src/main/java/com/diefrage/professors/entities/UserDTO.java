package com.diefrage.professors.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserDTO(
        @JsonProperty("id") Long id,
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String secondName,
        @JsonProperty("patronymic") String patronymic,
        @JsonProperty("email") String email,
        @JsonProperty("password") String password) {

    public static UserDTO fromUser(User user) {
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPatronymic(),
                user.getEmail(),
                user.getPassword()
        );
    }
}
