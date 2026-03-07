package com.challenge.library.repositories;

import com.challenge.library.entities.Book;
import com.challenge.library.entities.Student;

import java.util.HashMap;
import java.util.Map;

public class StudentRepository {

    private static final Map<Integer, Student> database = new HashMap<>();
    private static Integer nextId;


    public Student save(Student student) {
        if (student.getId() == null) {
            student.setId(nextId++);
        }
        database.put(student.getId(), student);
        return student;
    }
}


