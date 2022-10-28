package pro.sky.hwiii32.service;

import org.springframework.stereotype.Service;
import pro.sky.hwiii32.model.Faculty;
import pro.sky.hwiii32.model.Student;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private Map<Long, Faculty> faculties = new HashMap<>();
    private long lastId = 0;

    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(++lastId);
        faculties.put(lastId, faculty);
        return faculty;
    }

    public Faculty findFaculty(long id) {
        return faculties.get(id);
    }

    public Faculty editFaculty(Faculty faculty) {
        if (faculties.containsKey(faculty.getId())) {
            faculties.put(faculty.getId(), faculty);
            return faculty;
        }
        return null;
    }

    public Faculty deleteFaculty(long id){
        return faculties.remove(id);
    }

    public Collection<Faculty> getAll() {
        return new ArrayList<>(faculties.values());
    }

    public Collection<Faculty> getFacultiesWithEqualColor(String color) {
        return faculties.values().stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .collect(Collectors.toList());
    }
}
