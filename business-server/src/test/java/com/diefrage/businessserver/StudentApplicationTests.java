package com.diefrage.businessserver;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudentApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    public void testRegistration() throws Exception {
        Long studentId = 1L;
        String params = "{ \"email\": \"student@student.com\", \"name\": \"student\", \"group_number\": \"A1\" }";

        mockMvc.perform(post("/api/student/registration")
                        .content(params)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.student_id").exists());
    }

    @Test
    @Order(2)
    public void testGetStudentById() throws Exception {
        Long studentId = 1L;
        mockMvc.perform(get("/api/student/" + studentId))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void testGetStudentByEmail() throws Exception {
        mockMvc.perform(get("/api/student/").param("email", "student@student.com"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void testDeleteStudent() throws Exception {
        Long studentId = 1L;
        mockMvc.perform(delete("/api/student/delete/{student_id}", studentId))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    public void testGetStudentById_StudentNotFound() throws Exception {
        mockMvc.perform(get("/api/student/0"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("STUDENT_NOT_FOUND"));
    }

    @Test
    @Order(6)
    public void testDeleteStudent_StudentNotFound() throws Exception {
        mockMvc.perform(delete("/api/student/delete/0"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("STUDENT_NOT_FOUND"));
    }

    @Test
    @Order(7)
    public void testGetStudentByEmail_StudentNotFound() throws Exception {
        mockMvc.perform(get("/api/student/").param("email", "studentnotfound@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("STUDENT_NOT_FOUND"));
    }
}
