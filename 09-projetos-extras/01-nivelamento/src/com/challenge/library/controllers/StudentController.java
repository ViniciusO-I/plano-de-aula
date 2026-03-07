package com.challenge.library.controllers;

import com.challenge.library.entities.Student;
import com.challenge.library.services.StudentService;

public class StudentController {

    private StudentService service;


    public StudentController() {
        this.service = new StudentService();
    }

    public Student save(Student student) {
        return  service.save(student);
    }
}
