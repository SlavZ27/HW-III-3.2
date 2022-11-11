package pro.sky.hwiii32.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.hwiii32.record.FacultyRecord;
import pro.sky.hwiii32.record.StudentRecord;
import pro.sky.hwiii32.service.FacultyService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("faculty")
public class FacultyController {

    private final FacultyService facultyService;


    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping                //POST http://localhost:8080/faculty
    public ResponseEntity<FacultyRecord> createFaculty(@RequestBody @Valid FacultyRecord facultyRecord) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facultyService.createFaculty(facultyRecord));
    }

    @GetMapping("{id}")  //GET http://localhost:8080/faculty/1
    public ResponseEntity<FacultyRecord> readFaculty(@PathVariable Long id) {
        return ResponseEntity.ok(facultyService.readFaculty(id));
    }

    @PutMapping()               //PUT http://localhost:8080/faculty/
    public ResponseEntity<FacultyRecord> updateFaculty(@RequestBody @Valid FacultyRecord facultyRecord) {
        return ResponseEntity.ok(facultyService.updateFaculty(facultyRecord));
    }

    @DeleteMapping("{id}")    //DELETE http://localhost:8080/faculty/1
    public ResponseEntity<FacultyRecord> deleteFaculty(@PathVariable Long id) {
        return ResponseEntity.ok(facultyService.deleteFaculty(id));
    }

    @GetMapping()  //GET http://localhost:8080/faculty/
    public ResponseEntity<Collection<FacultyRecord>> getAllFaculty() {
        return ResponseEntity.ok(facultyService.getAll());
    }

    @GetMapping(params = "color")  //GET http://localhost:8080/faculty?color=blue
    public ResponseEntity<Collection<FacultyRecord>> getFacultiesWithEqualColor(@RequestParam(required = false) String color) {
        return ResponseEntity.ok(facultyService.getFacultiesWithEqualColor(color));
    }

    @GetMapping(params = "name-or-color")
    //GET http://localhost:8080/faculty?name-or-color=blue
    public ResponseEntity<Collection<FacultyRecord>> getFacultiesWithEqualNameOrColor(
            @RequestParam(value = "name-or-color", required = false) String nameOrColor) {
        return ResponseEntity.ok(facultyService.getFacultiesWithEqualNameOrColor(nameOrColor));
    }

    @GetMapping(path = {"{id}/student"})  //GET http://localhost:8080/faculty/1/student
    public ResponseEntity<Collection<StudentRecord>> getStudentsWithEqualFaculty(@PathVariable(value = "id", required = false) Long facultyId) {
        return ResponseEntity.ok(facultyService.getStudentsWithEqualFaculty(facultyId));
    }


}
