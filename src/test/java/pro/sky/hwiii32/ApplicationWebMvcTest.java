package pro.sky.hwiii32;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.hwiii32.component.RecordMapper;
import pro.sky.hwiii32.controller.AvatarController;
import pro.sky.hwiii32.controller.FacultyController;
import pro.sky.hwiii32.controller.StudentController;
import pro.sky.hwiii32.model.Faculty;
import pro.sky.hwiii32.model.Student;
import pro.sky.hwiii32.repository.AvatarRepository;
import pro.sky.hwiii32.repository.FacultyRepository;
import pro.sky.hwiii32.repository.StudentRepository;
import pro.sky.hwiii32.service.AvatarService;
import pro.sky.hwiii32.service.FacultyService;
import pro.sky.hwiii32.service.StudentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class ApplicationWebMvcTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AvatarRepository avatarRepository;
    @MockBean
    private FacultyRepository facultyRepository;
    @MockBean
    private StudentRepository studentRepository;
    @SpyBean
    private AvatarService avatarService;
    @SpyBean
    private FacultyService facultyService;
    @SpyBean
    private StudentService studentService;
    @SpyBean
    private RecordMapper recordMapper;
    @InjectMocks
    private AvatarController avatarController;
    @InjectMocks
    private FacultyController facultyController;
    @InjectMocks
    private StudentController studentController;

    @Test
    public void contextsLoad() {
        assertThat(avatarController).isNotNull();
        assertThat(facultyController).isNotNull();
        assertThat(studentController).isNotNull();
    }

    @Test
    public void createStudentTest() throws Exception {
        final Long id1 = 1L;
        final String name1 = "qwerty";
        final int age1 = 20;

        Student student1 = new Student();
        student1.setId(id1);
        student1.setName(name1);
        student1.setAge(age1);

        JSONObject studentJson1 = new JSONObject();
        studentJson1.put("name", name1);
        studentJson1.put("age", age1);

        when(studentRepository.save(any(Student.class))).thenReturn(student1);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(studentJson1.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id1))
                .andExpect(jsonPath("$.name").value(name1))
                .andExpect(jsonPath("$.age").value(age1));
    }

    @Test
    public void createStudentNegativeTest() throws Exception {
        final Long id1 = 1L;
        final String name1 = "";
        final int age1 = 20;

        Student student1 = new Student();
        student1.setId(id1);
        student1.setName(name1);
        student1.setAge(age1);

        JSONObject studentJson1 = new JSONObject();
        studentJson1.put("name", name1);
        studentJson1.put("age", age1);

        when(studentRepository.save(any(Student.class))).thenReturn(student1);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(studentJson1.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void readStudentTest() throws Exception {
        final Long id1 = 1L;
        final String name1 = "qwerty";
        final int age1 = 20;

        Student student1 = new Student();
        student1.setId(id1);
        student1.setName(name1);
        student1.setAge(age1);

        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/1")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id1))
                .andExpect(jsonPath("$.name").value(name1))
                .andExpect(jsonPath("$.age").value(age1));
    }

    @Test
    public void readStudentNegativeTest() throws Exception {
        when(studentRepository.findById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/2")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateStudentTest() throws Exception {
        final Long id1 = 1L;
        final String name1 = "qwerty";
        final int age1 = 20;

        Student student1 = new Student();
        student1.setId(id1);
        student1.setName(name1);
        student1.setAge(age1);

        JSONObject studentJson1 = new JSONObject();
        studentJson1.put("id", id1);
        studentJson1.put("name", name1);
        studentJson1.put("age", age1);

        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student1));
        when(studentRepository.save(any(Student.class))).thenReturn(student1);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student")
                        .content(studentJson1.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id1))
                .andExpect(jsonPath("$.name").value(name1))
                .andExpect(jsonPath("$.age").value(age1));
    }

    @Test
    public void updateStudentNegativeTest() throws Exception {
        final Long id1 = 1L;
        final String name1 = "qwerty";
        final int age1 = 20;

        JSONObject studentJson1 = new JSONObject();
        studentJson1.put("id", id1);
        studentJson1.put("name", name1);
        studentJson1.put("age", age1);

        when(studentRepository.findById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student")
                        .content(studentJson1.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteStudentTest() throws Exception {
        final Long id1 = 1L;
        final String name1 = "qwerty";
        final int age1 = 20;

        Student student1 = new Student();
        student1.setId(id1);
        student1.setName(name1);
        student1.setAge(age1);

        JSONObject studentJson1 = new JSONObject();
        studentJson1.put("id", id1);
        studentJson1.put("name", name1);
        studentJson1.put("age", age1);

        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student1));
        doNothing().when(studentRepository).deleteById(anyLong());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/1")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id1))
                .andExpect(jsonPath("$.name").value(name1))
                .andExpect(jsonPath("$.age").value(age1));
    }

    @Test
    public void deleteStudentNegativeTest() throws Exception {

        when(studentRepository.findById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/2")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllStudentTest() throws Exception {
        final Long id1 = 1L;
        final String name1 = "qwerty";
        final int age1 = 20;

        final Long id2 = 2L;
        final String name2 = "qwertyytrewq";
        final int age2 = 40;

        final Long id3 = 3L;
        final String name3 = "dfdsfgsdhga";
        final int age3 = 60;

        Student student1 = new Student();
        student1.setId(id1);
        student1.setName(name1);
        student1.setAge(age1);

        Student student2 = new Student();
        student2.setId(id2);
        student2.setName(name2);
        student2.setAge(age2);

        Student student3 = new Student();
        student3.setId(id3);
        student3.setName(name3);
        student3.setAge(age3);

        List<Student> studentList = new ArrayList<>();
        studentList.add(student1);
        studentList.add(student2);
        studentList.add(student3);

        when(studentRepository.findAll()).thenReturn(studentList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("[?(@.id==1)].name").value(name1))
                .andExpect(jsonPath("[?(@.id==1)].age").value(age1))
                .andExpect(jsonPath("[?(@.id==2)].name").value(name2))
                .andExpect(jsonPath("[?(@.id==2)].age").value(age2))
                .andExpect(jsonPath("[?(@.id==3)].name").value(name3))
                .andExpect(jsonPath("[?(@.id==3)].age").value(age3));
    }

    @Test
    public void getStudentsWithEqualAgeTest() throws Exception {
        final Long id1 = 1L;
        final String name1 = "qwerty";
        final int age1 = 30;

        final Long id2 = 2L;
        final String name2 = "qwertyytrewq";
        final int age2 = 30;

        final Long id3 = 3L;
        final String name3 = "dfdsfgsdhga";
        final int age3 = 30;

        Student student1 = new Student();
        student1.setId(id1);
        student1.setName(name1);
        student1.setAge(age1);

        Student student2 = new Student();
        student2.setId(id2);
        student2.setName(name2);
        student2.setAge(age2);

        Student student3 = new Student();
        student3.setId(id3);
        student3.setName(name3);
        student3.setAge(age3);

        List<Student> studentList = new ArrayList<>();
        studentList.add(student1);
        studentList.add(student2);
        studentList.add(student3);

        when(studentRepository.findStudentsByAge(anyInt())).thenReturn(studentList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/filter?age=30")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("[?(@.id==1)].name").value(name1))
                .andExpect(jsonPath("[?(@.id==1)].age").value(age1))
                .andExpect(jsonPath("[?(@.id==2)].name").value(name2))
                .andExpect(jsonPath("[?(@.id==2)].age").value(age2))
                .andExpect(jsonPath("[?(@.id==3)].name").value(name3))
                .andExpect(jsonPath("[?(@.id==3)].age").value(age3));
    }

    @Test
    public void getStudentsWithBetweenAgeTest() throws Exception {
        final Long id1 = 1L;
        final String name1 = "qwerty";
        final int age1 = 30;

        final Long id2 = 2L;
        final String name2 = "qwertyytrewq";
        final int age2 = 30;

        final Long id3 = 3L;
        final String name3 = "dfdsfgsdhga";
        final int age3 = 30;

        Student student1 = new Student();
        student1.setId(id1);
        student1.setName(name1);
        student1.setAge(age1);

        Student student2 = new Student();
        student2.setId(id2);
        student2.setName(name2);
        student2.setAge(age2);

        Student student3 = new Student();
        student3.setId(id3);
        student3.setName(name3);
        student3.setAge(age3);

        List<Student> studentList = new ArrayList<>();
        studentList.add(student1);
        studentList.add(student2);
        studentList.add(student3);

        when(studentRepository.findStudentsByAgeBetween(anyInt(), anyInt())).thenReturn(studentList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/filter?age_from=3&age_to=5")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("[?(@.id==1)].name").value(name1))
                .andExpect(jsonPath("[?(@.id==1)].age").value(age1))
                .andExpect(jsonPath("[?(@.id==2)].name").value(name2))
                .andExpect(jsonPath("[?(@.id==2)].age").value(age2))
                .andExpect(jsonPath("[?(@.id==3)].name").value(name3))
                .andExpect(jsonPath("[?(@.id==3)].age").value(age3));
    }

    @Test
    public void getFacultyByStudentTest() throws Exception {
        final Long id1 = 1L;
        final String name1 = "qwerty";
        final int age1 = 30;

        final Long fId2 = 2L;
        final String fName2 = "sadasfasf";
        final String fColor2 = "blue";

        Faculty faculty = new Faculty();
        faculty.setId(fId2);
        faculty.setName(fName2);
        faculty.setColor(fColor2);

        Student student1 = new Student();
        student1.setId(id1);
        student1.setName(name1);
        student1.setAge(age1);
        student1.setFaculty(faculty);

        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/filter?student_id=1")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(fId2))
                .andExpect(jsonPath("$.name").value(fName2))
                .andExpect(jsonPath("$.color").value(fColor2));
    }

    @Test
    public void getFacultyByStudentNegativeTest() throws Exception {

        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/filter?student_id=1")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }
/////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void createFacultyTest() throws Exception {
        final Long id1 = 1L;
        final String name1 = "qwerty";
        final String color1 = "blue";

        Faculty faculty1 = new Faculty();
        faculty1.setId(id1);
        faculty1.setName(name1);
        faculty1.setColor(color1);

        JSONObject facultyJson1 = new JSONObject();
        facultyJson1.put("name", name1);
        facultyJson1.put("color", color1);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty1);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyJson1.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id1))
                .andExpect(jsonPath("$.name").value(name1))
                .andExpect(jsonPath("$.color").value(color1));
    }

    @Test
    public void createFacultyNegativeTest() throws Exception {
        final Long id1 = 1L;
        final String name1 = "";
        final String color1 = "blue";

        Faculty faculty1 = new Faculty();
        faculty1.setId(id1);
        faculty1.setName(name1);
        faculty1.setColor(color1);

        JSONObject facultyJson1 = new JSONObject();
        facultyJson1.put("name", name1);
        facultyJson1.put("color", color1);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty1);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyJson1.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void readFacultyTest() throws Exception {
        final Long id1 = 1L;
        final String name1 = "qwerty";
        final String color1 = "blue";

        Faculty faculty1 = new Faculty();
        faculty1.setId(id1);
        faculty1.setName(name1);
        faculty1.setColor(color1);

        when(facultyRepository.findById(anyLong())).thenReturn(Optional.of(faculty1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/1")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id1))
                .andExpect(jsonPath("$.name").value(name1))
                .andExpect(jsonPath("$.color").value(color1));
    }

    @Test
    public void readFacultyNegativeTest() throws Exception {
        when(facultyRepository.findById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/2")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateFacultyTest() throws Exception {
        final Long id1 = 1L;
        final String name1 = "qwerty";
        final String color1 = "blue";

        Faculty faculty1 = new Faculty();
        faculty1.setId(id1);
        faculty1.setName(name1);
        faculty1.setColor(color1);

        JSONObject facultyJson1 = new JSONObject();
        facultyJson1.put("id", id1);
        facultyJson1.put("name", name1);
        facultyJson1.put("color", color1);

        when(facultyRepository.findById(anyLong())).thenReturn(Optional.of(faculty1));
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty1);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(facultyJson1.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id1))
                .andExpect(jsonPath("$.name").value(name1))
                .andExpect(jsonPath("$.color").value(color1));
    }

    @Test
    public void updateFacultyNegativeTest() throws Exception {
        final Long id1 = 2L;
        final String name1 = "qwerty";
        final String color1 = "blue";

        JSONObject facultyJson1 = new JSONObject();
        facultyJson1.put("id", id1);
        facultyJson1.put("name", name1);
        facultyJson1.put("color", color1);

        when(facultyRepository.findById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(facultyJson1.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteFacultyTest() throws Exception {
        final Long id1 = 1L;
        final String name1 = "qwerty";
        final String color1 = "blue";

        Faculty faculty1 = new Faculty();
        faculty1.setId(id1);
        faculty1.setName(name1);
        faculty1.setColor(color1);

        JSONObject facultyJson1 = new JSONObject();
        facultyJson1.put("id", id1);
        facultyJson1.put("name", name1);
        facultyJson1.put("color", color1);

        when(facultyRepository.findById(anyLong())).thenReturn(Optional.of(faculty1));
        doNothing().when(facultyRepository).deleteById(anyLong());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/1")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id1))
                .andExpect(jsonPath("$.name").value(name1))
                .andExpect(jsonPath("$.color").value(color1));
    }

    @Test
    public void deleteFacultyNegativeTest() throws Exception {

        when(facultyRepository.findById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/2")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllFacultyTest() throws Exception {
        final Long id1 = 1L;
        final String name1 = "qwerty";
        final String color1 = "blue";

        final Long id2 = 2L;
        final String name2 = "qwertyytrewq";
        final String color2 = "red";

        final Long id3 = 3L;
        final String name3 = "dfdsfgsdhga";
        final String color3 = "blue";

        Faculty faculty1 = new Faculty();
        faculty1.setId(id1);
        faculty1.setName(name1);
        faculty1.setColor(color1);

        Faculty faculty2 = new Faculty();
        faculty2.setId(id2);
        faculty2.setName(name2);
        faculty2.setColor(color2);

        Faculty faculty3 = new Faculty();
        faculty3.setId(id3);
        faculty3.setName(name3);
        faculty3.setColor(color3);

        List<Faculty> facultyList = new ArrayList<>();
        facultyList.add(faculty1);
        facultyList.add(faculty2);
        facultyList.add(faculty3);

        when(facultyRepository.findAll()).thenReturn(facultyList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("[?(@.id==1)].name").value(name1))
                .andExpect(jsonPath("[?(@.id==1)].color").value(color1))
                .andExpect(jsonPath("[?(@.id==2)].name").value(name2))
                .andExpect(jsonPath("[?(@.id==2)].color").value(color2))
                .andExpect(jsonPath("[?(@.id==3)].name").value(name3))
                .andExpect(jsonPath("[?(@.id==3)].color").value(color3));
    }

    @Test
    public void getFacultiesWithEqualColorTest() throws Exception {
        final Long id1 = 1L;
        final String name1 = "qwerty";
        final String color1 = "blue";

        final Long id2 = 2L;
        final String name2 = "qwertyytrewq";
        final String color2 = "blue";

        final Long id3 = 3L;
        final String name3 = "dfdsfgsdhga";
        final String color3 = "blue";

        Faculty faculty1 = new Faculty();
        faculty1.setId(id1);
        faculty1.setName(name1);
        faculty1.setColor(color1);

        Faculty faculty2 = new Faculty();
        faculty2.setId(id2);
        faculty2.setName(name2);
        faculty2.setColor(color2);

        Faculty faculty3 = new Faculty();
        faculty3.setId(id3);
        faculty3.setName(name3);
        faculty3.setColor(color3);

        List<Faculty> facultyList = new ArrayList<>();
        facultyList.add(faculty1);
        facultyList.add(faculty2);
        facultyList.add(faculty3);

        when(facultyRepository.findFacultiesByColor(anyString())).thenReturn(facultyList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/filter?color=blue")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("[?(@.id==1)].name").value(name1))
                .andExpect(jsonPath("[?(@.id==1)].color").value(color1))
                .andExpect(jsonPath("[?(@.id==2)].name").value(name2))
                .andExpect(jsonPath("[?(@.id==2)].color").value(color2))
                .andExpect(jsonPath("[?(@.id==3)].name").value(name3))
                .andExpect(jsonPath("[?(@.id==3)].color").value(color3));
    }

    @Test
    public void getFacultiesWithEqualNameOrColorTest() throws Exception {
        final Long id1 = 1L;
        final String name1 = "qwerty";
        final String color1 = "blue";

        final Long id2 = 2L;
        final String name2 = "qwertyytrewq";
        final String color2 = "blue";

        final Long id3 = 3L;
        final String name3 = "dfdsfgsdhga";
        final String color3 = "blue";

        Faculty faculty1 = new Faculty();
        faculty1.setId(id1);
        faculty1.setName(name1);
        faculty1.setColor(color1);

        Faculty faculty2 = new Faculty();
        faculty2.setId(id2);
        faculty2.setName(name2);
        faculty2.setColor(color2);

        Faculty faculty3 = new Faculty();
        faculty3.setId(id3);
        faculty3.setName(name3);
        faculty3.setColor(color3);

        List<Faculty> facultyList = new ArrayList<>();
        facultyList.add(faculty1);
        facultyList.add(faculty2);
        facultyList.add(faculty3);

        when(facultyRepository.
                findFacultiesByNameIgnoreCaseOrColorIgnoreCase(anyString(), anyString()))
                .thenReturn(facultyList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/filter?name=name&color=blue")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("[?(@.id==1)].name").value(name1))
                .andExpect(jsonPath("[?(@.id==1)].color").value(color1))
                .andExpect(jsonPath("[?(@.id==2)].name").value(name2))
                .andExpect(jsonPath("[?(@.id==2)].color").value(color2))
                .andExpect(jsonPath("[?(@.id==3)].name").value(name3))
                .andExpect(jsonPath("[?(@.id==3)].color").value(color3));
    }

    @Test
    public void getStudentsWithEqualFacultyTest() throws Exception {
        final Long id1 = 1L;
        final String name1 = "qwerty";
        final String color1 = "blue";

        Faculty faculty1 = new Faculty();
        faculty1.setId(id1);
        faculty1.setName(name1);
        faculty1.setColor(color1);

        final Long sId1 = 1L;
        final String sName1 = "qwerty";
        final int sAge1 = 20;

        final Long sId2 = 2L;
        final String sName2 = "qwertyytrewq";
        final int sAge2 = 40;

        final Long sId3 = 3L;
        final String sName3 = "dfdsfgsdhga";
        final int sAge3 = 60;

        Student student1 = new Student();
        student1.setId(sId1);
        student1.setName(sName1);
        student1.setAge(sAge1);
        student1.setFaculty(faculty1);

        Student student2 = new Student();
        student2.setId(sId2);
        student2.setName(sName2);
        student2.setAge(sAge2);
        student2.setFaculty(faculty1);


        Student student3 = new Student();
        student3.setId(sId3);
        student3.setName(sName3);
        student3.setAge(sAge3);
        student3.setFaculty(faculty1);


        List<Student> studentList = new ArrayList<>();
        studentList.add(student1);
        studentList.add(student2);
        studentList.add(student3);

        when(studentRepository.findStudentsByFacultyId(anyLong()))
                .thenReturn(studentList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/filter?faculty_id=15")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("[?(@.id==1)].name").value(sName1))
                .andExpect(jsonPath("[?(@.id==1)].age").value(sAge1))
                .andExpect(jsonPath("[?(@.id==2)].name").value(sName2))
                .andExpect(jsonPath("[?(@.id==2)].age").value(sAge2))
                .andExpect(jsonPath("[?(@.id==3)].name").value(sName3))
                .andExpect(jsonPath("[?(@.id==3)].age").value(sAge3));
    }
}