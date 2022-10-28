package pro.sky.hwiii32.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.hwiii32.model.Faculty;
import pro.sky.hwiii32.service.FacultyService;

import java.util.Collection;

@RestController
@RequestMapping("faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping()  //GET http://localhost:8080/faculty/
    public ResponseEntity<Collection<Faculty>> getAllFaculty() {
        return ResponseEntity.ok(facultyService.getAll());
    }

    @GetMapping("{id}")  //GET http://localhost:8080/faculty/1
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long id) {
        Faculty faculty = facultyService.findFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @GetMapping("/color/{color}")  //GET http://localhost:8080/faculty?color=20
    public ResponseEntity<Collection<Faculty>> getStudentsWithEqualAge(@RequestParam String color) {
        return ResponseEntity.ok(facultyService.getFacultiesWithEqualColor(color));
    }

    @PostMapping                //POST http://localhost:8080/faculty
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        Faculty createdFaculty = facultyService.createFaculty(faculty);
        return ResponseEntity.ok(createdFaculty);
    }

    @PutMapping()               //PUT http://localhost:8080/faculty/
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty foundFaculty = facultyService.editFaculty(faculty);
        if (foundFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundFaculty);
    }

    @DeleteMapping("{id}")      //DELETE http://localhost:8080/faculty/1
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable Long id) {
        return ResponseEntity.ok(facultyService.deleteFaculty(id));
    }
}
