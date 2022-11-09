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

    @GetMapping(value = "filter", params = "color")  //GET http://localhost:8080/faculty/filter?color=blue
    public ResponseEntity<Collection<FacultyRecord>> getFacultiesWithEqualColor(@RequestParam String color) {
        return ResponseEntity.ok(facultyService.getFacultiesWithEqualColor(color));
    }

    @GetMapping(value = "filter", params = {"name", "color"})
    //GET http://localhost:8080/faculty/filter?name=name&color=blue
    public ResponseEntity<Collection<FacultyRecord>> getFacultiesWithEqualNameOrColor(
            @RequestParam(required = false) String name
            , @RequestParam(required = false) String color) {
        return ResponseEntity.ok(facultyService.getFacultiesWithEqualNameOrColor(name, color));
    }

    @GetMapping(value = "filter", params = "faculty_id")  //GET http://localhost:8080/faculty/filter?faculty_id=15
    public ResponseEntity<Collection<StudentRecord>> getStudentsWithEqualFaculty(@RequestParam("faculty_id") Long facultyId) {
        return ResponseEntity.ok(facultyService.getStudentsWithEqualFaculty(facultyId));
    }


}
