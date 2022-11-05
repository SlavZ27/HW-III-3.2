package pro.sky.hwiii32.record;

import pro.sky.hwiii32.model.Student;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

public class FacultyRecord {

    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private String color;
    @NotNull
    private Set<Student> students;

    public Set<Student> getStudents() {
        return new HashSet<>(students);
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
