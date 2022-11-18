package pro.sky.hwiii32.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.hwiii32.model.Avatar;
import pro.sky.hwiii32.record.AvatarRecord;
import pro.sky.hwiii32.service.AvatarService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("avatar")
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    //POST http://localhost:8080/avatar/1
    @PostMapping(value = "{student-id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AvatarRecord> createAvatar(@PathVariable("student-id") Long idStudent,
                                                     @RequestParam MultipartFile avatarFile) throws IOException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(avatarService.createAvatar(idStudent, avatarFile));
    }

    //GET http://localhost:8080/avatar/1/avatar-dir
    @GetMapping(value = "{id}/avatar-dir")
    public void readAvatarFromDir(@PathVariable Long id,
                                  HttpServletResponse response) throws IOException {
        avatarService.readAvatarFromDir(id, response);
    }

    //GET http://localhost:8080/avatar/1/avatar-db
    @GetMapping(value = "{id}/avatar-db")
    public ResponseEntity<byte[]> readAvatarFromDb(@PathVariable Long id) {
        Avatar avatar = avatarService.readAvatarDb(id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        httpHeaders.setContentLength(avatar.getFileSize());
        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(avatar.getData());
    }

    //DELETE http://localhost:8080/avatar/1
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteAvatar(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(avatarService.deleteAvatar(id));
    }

    //GET http://localhost:8080/avatar?page-number=1&page-size=10
    @GetMapping()
    public ResponseEntity<List<AvatarRecord>> getAllAvatarWithPagination(@RequestParam("page-number") Integer pageNumber,
                                                                         @RequestParam("page-size") Integer pageSize){
        return ResponseEntity.status(HttpStatus.OK)
                .body(avatarService.getAllAvatarWithPagination(pageNumber, pageSize));
    }

}