package com.challenge.library.services;

import com.challenge.library.entities.Student;
import com.challenge.library.repositories.StudentRepository;

public class StudentService {



    private StudentRepository repository;

    public StudentService() {
        this.repository = new StudentRepository();
    }

    public Student save(Student student) {
        return repository.save(student);
    }
}
