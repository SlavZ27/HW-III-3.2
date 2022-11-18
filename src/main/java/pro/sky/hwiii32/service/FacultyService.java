package pro.sky.hwiii32.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
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
    private final static Logger logger = LoggerFactory.getLogger(FacultyService.class);


    public FacultyService(FacultyRepository facultyRepository, RecordMapper recordMapper) {
        this.facultyRepository = facultyRepository;
        this.recordMapper = recordMapper;
    }

    public Faculty findFacultyById(Long id) {
        logger.info("Was invoked method findFacultyById for find faculty by id = {}", id);

        Optional<Faculty> facultyOptional =
                facultyRepository.findById(id);

        if (facultyOptional.isPresent()) {
            logger.debug("Faculty with id = {} was found", id);
        }

        return facultyOptional.orElseThrow(() -> new FacultyNotFoundException(String.valueOf(id)));
    }

    public FacultyRecord createFaculty(FacultyRecord facultyRecord) {
        logger.info("Was invoked method createFaculty for create faculty");
        Faculty facultyEntity = recordMapper.toEntity(facultyRecord);
        Faculty facultyCreate = new Faculty();
        facultyCreate.setName(facultyEntity.getName());
        facultyCreate.setColor(facultyEntity.getColor());

        return recordMapper.toRecord(
                facultyRepository.save(facultyCreate));
    }

    public FacultyRecord readFaculty(long id) {
        logger.info("Was invoked method readFaculty for read faculty by id = {}", id);
        return recordMapper.toRecord(findFacultyById(id));
    }

    public FacultyRecord updateFaculty(FacultyRecord facultyRecord) {
        logger.info("Was invoked method updateFaculty for update faculty");
        Faculty oldFaculty = findFacultyById(facultyRecord.getId());
        oldFaculty.setName(facultyRecord.getName());
        oldFaculty.setColor(facultyRecord.getColor());
        return recordMapper.toRecord(
                facultyRepository.save(oldFaculty));
    }

    public FacultyRecord deleteFaculty(long id) {
        logger.info("Was invoked method deleteFaculty for delete faculty by id = {}", id);
        Faculty foundFaculty = findFacultyById(id);
        facultyRepository.deleteById(id);
        return recordMapper.toRecord(foundFaculty);
    }

    public Collection<FacultyRecord> getAll() {
        logger.info("Was invoked method getAll for send all faculty");
        return facultyRepository.findAll().stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public Collection<FacultyRecord> getFacultiesWithEqualColor(String needColor) {
        logger.info("Was invoked method getFacultiesWithEqualColor " +
                "for send all faculty with the desired color = {}", needColor);
        return facultyRepository.findFacultiesByColor(needColor).stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public Collection<FacultyRecord> getFacultiesWithEqualNameOrColor(String nameOrColor) {
        logger.info("Was invoked method getFacultiesWithEqualNameOrColor " +
                "for send all faculty with the desired color or name with parameter = {}", nameOrColor);
        return facultyRepository.findFacultiesByNameIgnoreCaseOrColorIgnoreCase(nameOrColor, nameOrColor).stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public Collection<StudentRecord> getStudentsWithEqualFaculty(Long facultyId) {
        logger.info("Was invoked method getStudentsWithEqualFaculty " +
                "for send list of students by faculty with id = {}", facultyId);
        return facultyRepository.findById(facultyId)
                .orElseThrow(() -> new FacultyNotFoundException(String.valueOf(facultyId)))
                .getStudents().stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public String getLongestNameFaculty() {
        logger.info("Was invoked method getLongestNameFaculty " +
                "for send longest name of faculty");
        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparing(String::length))
                .orElseThrow(() -> new NotFoundException("Список пуст"));
    }

}
