package com.springsecurity.io.controller;

import com.springsecurity.io.entity.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentController {

    private List<Student> students = new ArrayList<>(List.of(
            new Student(1,"Rohit",45),
            new Student(2,"Kunvar",60),
            new Student(2,"Isha",60)
    ));

    @GetMapping("/students")
   public List<Student>getAll(){
       return students;
   }

   @PostMapping("/studenst")

    public Student add(@RequestBody Student student){
        students.add(student);
        return student;
   }
}
