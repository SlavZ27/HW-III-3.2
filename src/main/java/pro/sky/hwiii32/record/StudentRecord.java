package pro.sky.hwiii32.record;

import pro.sky.hwiii32.model.Faculty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class StudentRecord {

    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private int age;
    @NotNull
    private Faculty faculty;

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
