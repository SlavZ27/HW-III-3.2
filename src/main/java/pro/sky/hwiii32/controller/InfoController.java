package pro.sky.hwiii32.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.hwiii32.service.InfoService;


@RestController
public class InfoController {

    private final InfoService infoService;

    public InfoController(InfoService infoService) {
        this.infoService = infoService;
    }

    //GET http://localhost:8080/getPort
    @GetMapping("getPort")
    public ResponseEntity<Integer> getPort() {
        return ResponseEntity.ok(infoService.getPort());
    }


}
