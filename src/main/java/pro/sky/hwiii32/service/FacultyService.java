package pro.sky.hwiii32.service;

import org.springframework.stereotype.Service;
import pro.sky.hwiii32.component.RecordMapper;
import pro.sky.hwiii32.exceptions.FacultyNotFoundException;
import pro.sky.hwiii32.model.Faculty;
import pro.sky.hwiii32.record.FacultyRecord;
import pro.sky.hwiii32.record.StudentRecord;
import pro.sky.hwiii32.repository.FacultyRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final RecordMapper recordMapper;


    public FacultyService(FacultyRepository facultyRepository, RecordMapper recordMapper) {
        this.facultyRepository = facultyRepository;
        this.recordMapper = recordMapper;
    }

    public Faculty findFacultyById(Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new FacultyNotFoundException(String.valueOf(id)));
    }

    public FacultyRecord createFaculty(FacultyRecord facultyRecord) {
        Faculty facultyEntity = recordMapper.toEntity(facultyRecord);
        Faculty facultyCreate = new Faculty();
        facultyCreate.setName(facultyEntity.getName());
        facultyCreate.setColor(facultyEntity.getColor());
        if (facultyEntity.getStudents() != null) {
            facultyCreate.setStudents(facultyEntity.getStudents());
        }
        return recordMapper.toRecord(
                facultyRepository.save(facultyCreate));
    }

    public FacultyRecord readFaculty(long id) {
        return recordMapper.toRecord(findFacultyById(id));
    }

    public FacultyRecord updateFaculty(FacultyRecord facultyRecord) {
        Faculty oldFaculty = findFacultyById(facultyRecord.getId());
        oldFaculty.setName(facultyRecord.getName());
        oldFaculty.setColor(facultyRecord.getColor());
        return recordMapper.toRecord(
                facultyRepository.save(oldFaculty));
    }

    public FacultyRecord deleteFaculty(long id) {
        Faculty foundFaculty = findFacultyById(id);
        facultyRepository.deleteById(id);
        return recordMapper.toRecord(foundFaculty);
    }

    public Collection<FacultyRecord> getAll() {
        return facultyRepository.findAll().stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public Collection<FacultyRecord> getFacultiesWithEqualColor(String needColor) {
        return facultyRepository.findFacultiesByColor(needColor).stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public Collection<FacultyRecord> getFacultiesWithEqualNameOrColor(String nameOrColor) {
        return facultyRepository.findFacultiesByNameIgnoreCaseOrColorIgnoreCase(nameOrColor, nameOrColor).stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public Collection<StudentRecord> getStudentsWithEqualFaculty(Long facultyId) {
        return facultyRepository.findById(facultyId)
                .orElseThrow(() -> new FacultyNotFoundException(String.valueOf(facultyId)))
                .getStudents().stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }
}
