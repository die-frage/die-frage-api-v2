package com.diefrage.student.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StudentDTO(
        @JsonProperty("student_id") Long id,
        @JsonProperty("name") String name,
        @JsonProperty("group_number") String groupNumber,
        @JsonProperty("email") String email) {

    public static StudentDTO fromStudent(Student student) {
        return new StudentDTO(
                student.getStudentId(),
                student.getName(),
                student.getGroupNumber(),
                student.getEmail());
    }
}
