package pro.sky.hwiii32.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.hwiii32.model.Student;
import pro.sky.hwiii32.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping()  //GET http://localhost:8080/student/
    public ResponseEntity<Collection<Student>> getAllStudent() {
        return ResponseEntity.ok(studentService.getAll());
    }

    @GetMapping("{id}")  //GET http://localhost:8080/student/1
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Student Student = studentService.findStudent(id);
        if (Student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Student);
    }

    @GetMapping("/age/{age}")  //GET http://localhost:8080/student?age=20
    public ResponseEntity<Collection<Student>> getStudentsWithEqualAge(@RequestParam Integer age) {
        return ResponseEntity.ok(studentService.getStudentsWithEqualAge(age));
    }

    @PostMapping                //POST http://localhost:8080/student
    public ResponseEntity<Student> createStudent(@RequestBody Student Student) {
        Student createdStudent = studentService.createStudent(Student);
        return ResponseEntity.ok(createdStudent);
    }

    @PutMapping()               //PUT http://localhost:8080/student/
    public ResponseEntity<Student> editStudent(@RequestBody Student Student) {
        Student foundStudent = studentService.editStudent(Student);
        if (foundStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("{id}")      //DELETE http://localhost:8080/student/1
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.deleteStudent(id));
    }

}