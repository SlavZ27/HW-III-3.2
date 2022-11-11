package pro.sky.hwiii32.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.hwiii32.record.FacultyRecord;
import pro.sky.hwiii32.record.StudentRecord;
import pro.sky.hwiii32.service.StudentService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping                //POST http://localhost:8080/student
    public ResponseEntity<StudentRecord> createStudent(@RequestBody @Valid StudentRecord studentRecord) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studentService.createStudent(studentRecord));
    }

    @GetMapping("{id}")  //GET http://localhost:8080/student/1
    public ResponseEntity<StudentRecord> readStudent(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.readStudent(id));
    }

    @PutMapping()               //PUT http://localhost:8080/student/
    public ResponseEntity<StudentRecord> updateStudent(@RequestBody @Valid StudentRecord studentRecord) {
        return ResponseEntity.ok(studentService.updateStudent(studentRecord));

    }

    @DeleteMapping("{id}")      //DELETE http://localhost:8080/student/1
    public ResponseEntity<StudentRecord> deleteStudent(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.deleteStudent(id));
    }


    @GetMapping()  //GET http://localhost:8080/student/
    public ResponseEntity<Collection<StudentRecord>> getAllStudent() {
        return ResponseEntity.ok(studentService.getAll());
    }

    @GetMapping(params = "age")  //GET http://localhost:8080/student?age=3
    public ResponseEntity<Collection<StudentRecord>> getStudentsWithEqualAge(@RequestParam(required = false) Integer age) {
        return ResponseEntity.ok(studentService.getStudentsWithEqualAge(age));
    }

    @GetMapping(params = {"age_from", "age_to"})
    //GET http://localhost:8080/studentage_from=3&age_to=5
    public ResponseEntity<Collection<StudentRecord>> getStudentsWithBetweenAge(
            @RequestParam(value = "age_from",required = false) Integer ageFrom
            , @RequestParam(value = "age_to",required = false) Integer ageTo) {
        return ResponseEntity.ok(studentService.getStudentsWithBetweenAge(ageFrom, ageTo));
    }

    @GetMapping(path = {"{id}/faculty"})  //GET http://localhost:8080/student/1/faculty
    public ResponseEntity<FacultyRecord> getFacultyByStudent(@PathVariable(value = "id") Long studentId) {
        return ResponseEntity.ok(studentService.getFacultyByStudent(studentId));
    }

    @GetMapping("count")  //GET http://localhost:8080/student/count
    public ResponseEntity<Long> getCountStudent() {
        return ResponseEntity.ok(studentService.getCountStudent());
    }

    @GetMapping("mid-age")  //GET http://localhost:8080/student/mid-age
    public ResponseEntity<Float> getMidAgeOfStudent() {
        return ResponseEntity.ok(studentService.getMidAgeOfStudent());
    }

    @GetMapping("5BiggerId")  //GET http://localhost:8080/student/5BiggerId
    public ResponseEntity<List<StudentRecord>> get5StudentWithBiggerId() {
        return ResponseEntity.ok(studentService.get5StudentWithBiggerId());
    }

}