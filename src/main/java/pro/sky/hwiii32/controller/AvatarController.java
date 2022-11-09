package pro.sky.hwiii32.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.hwiii32.model.Avatar;
import pro.sky.hwiii32.service.AvatarService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("student")
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    //POST http://localhost:8080/student/{id}/avatar
    @PostMapping(value = "{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createAvatar(@PathVariable Long id,
                                               @RequestParam MultipartFile avatarFile) throws IOException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(avatarService.createAvatar(id, avatarFile));
    }

    //GET http://localhost:8080/student/1/avatar-preview
    @GetMapping(value = "{id}/avatar-db")
    public ResponseEntity<byte[]> readAvatarFromDb(@PathVariable Long id) {
        Avatar avatar = avatarService.readAvatarDb(id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        httpHeaders.setContentLength(avatar.getFileSize());
        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(avatar.getData());
    }

    @GetMapping(value = "{id}/avatar-dir")
    public void readAvatarFromDir(@PathVariable Long id,
                                  HttpServletResponse response) throws IOException {
        avatarService.readAvatarFromDir(id, response);
    }

    //DELETE http://localhost:8080/student/1/avatar
    @DeleteMapping("{id}/avatar")
    public ResponseEntity<String> deleteAvatar(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(avatarService.deleteAvatar(id));
    }

}