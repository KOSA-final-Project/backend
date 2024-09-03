package site.hesil.latteve_spring.global.amazon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import site.hesil.latteve_spring.global.amazon.service.S3Service;

/**
 * packageName    : site.hesil.latteve_spring.global.amazon.controller
 * fileName       : S3Controller
 * author         : Yeong-Huns
 * date           : 2024-09-02
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-02        Yeong-Huns       최초 생성
 */
@Log4j2
@RequiredArgsConstructor
@RequestMapping("api/file")
@RestController
public class S3Controller {
    private final S3Service s3Service;


    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String result = s3Service.uploadFile(file);
        log.info("업로드에 성공했습니다!{}", result);
        return ResponseEntity.ok(result);
    }

}
