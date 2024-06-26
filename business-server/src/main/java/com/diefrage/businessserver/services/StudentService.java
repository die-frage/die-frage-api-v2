package com.diefrage.businessserver.services;

import com.diefrage.businessserver.entities.Student;
import com.diefrage.businessserver.repositories.StudentRepository;
import com.diefrage.businessserver.requests.StudentRequest;
import com.diefrage.exceptions.TypicalServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public Student getStudentById(Long studentId) {
        Optional<Student> item = studentRepository.findById(studentId);
        if (item.isEmpty()) {
            TypicalServerException.STUDENT_NOT_FOUND.throwException();
        }
        return item.get();
    }

    public Student getStudentByEmail(String email) {
        Optional<Student> item = studentRepository.findByEmail(email);
        if (item.isEmpty()) {
            TypicalServerException.STUDENT_NOT_FOUND.throwException();
        }
        return item.get();
    }

    public Student register(StudentRequest request) {
        if (!isValidEmail(request.getEmail())) {
            TypicalServerException.INVALID_EMAIL_FORMAT.throwException();
        }
        Student student = new Student();
        student.setEmail(request.getEmail());
        student.setName(request.getName());
        student.setGroupNumber(request.getGroup_number());
        student.setChatId(request.getChat_id());
        studentRepository.save(student);
        return student;
    }

    public Student deleteStudent(Long studentId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isEmpty()) {
            TypicalServerException.STUDENT_NOT_FOUND.throwException();
        }
        studentRepository.deleteById(studentId);
        return studentOptional.get();

    }

    private boolean isValidEmail(String email) {
        String emailRegex = ".{2,}@.{2,}";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public Student update(StudentRequest request) {
        if (!isValidEmail(request.getEmail())) {
            TypicalServerException.INVALID_EMAIL_FORMAT.throwException();
        }

        Optional<Student> studentOptional = studentRepository.findByEmail(request.getEmail());
        if (studentOptional.isEmpty()) TypicalServerException.STUDENT_NOT_FOUND.throwException();
        Student student = studentOptional.get();

        student.setEmail(request.getEmail());
        student.setName(request.getName());
        student.setGroupNumber(request.getGroup_number());
        student.setChatId(request.getChat_id());

        studentRepository.save(student);
        return student;
    }

    public Student getStudentByChatId(String chatId) {
        Optional<Student> item = studentRepository.findByChatId(chatId);
        if (item.isEmpty()) {
            TypicalServerException.STUDENT_NOT_FOUND.throwException();
        }
        return item.get();
    }
}
