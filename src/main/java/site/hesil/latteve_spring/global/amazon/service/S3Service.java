package site.hesil.latteve_spring.global.amazon.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.hesil.latteve_spring.global.error.errorcode.ErrorCode;
import site.hesil.latteve_spring.global.error.exception.S3UploadException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

/**
 * packageName    : site.hesil.latteve_spring.global.amazon.service
 * fileName       : S3Service
 * author         : Yeong-Huns
 * date           : 2024-08-24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-24        Yeong-Huns       최초 생성
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.dir}")
    private String dir;

    public String uploadFile(MultipartFile multipartFile) {
        String originalFileName = multipartFile.getOriginalFilename(); // 업로드한 파일명
        String uploadFileName = generateFileName(Objects.requireNonNull(originalFileName));

        ObjectMetadata objectMetadata = createObjectMetadata(multipartFile);

        String keyName = dir + "/" + uploadFileName; // S3 에 저장될 파일의 경로 및 이름

        try (InputStream inputStream = multipartFile.getInputStream()) { // MultipartFile -> InputStream
            uploadToS3(inputStream, keyName, objectMetadata);
            return amazonS3.getUrl(bucket, keyName).toString();
        } catch (IOException e) {
            log.error("파일 업로드에 실패하였습니다. ", e);
            throw new S3UploadException(ErrorCode.S3_IMAGE_UPLOAD_FAIL, e);
        }
    }

    public String updateFile(MultipartFile file, String existingFileName){ // 업데이트(수정)
        String keyName = dir + "/" + existingFileName;

        ObjectMetadata objectMetadata = createObjectMetadata(file);

        try (InputStream inputStream = file.getInputStream()) {
            uploadToS3(inputStream, keyName, objectMetadata);
            return amazonS3.getUrl(bucket, keyName).toString();
        } catch (IOException e) {
            log.error("파일 업데이트에 실패하였습니다. ", e);
            throw new S3UploadException("S3 버킷의 파일 업데이트에 실패하였습니다.", e);
        }
    }

    public void deleteFile(String filePath) { // 삭제
        if (filePath != null && !filePath.isEmpty()) {
            boolean isExistObject = amazonS3.doesObjectExist(bucket, filePath);
            if (isExistObject) {
                amazonS3.deleteObject(bucket, filePath);
                log.info("S3 버킷에서 파일을 삭제하는데 성공하였습니다. : {}", filePath);
            } else {
                log.warn("S3 버킷에 해당 파일이 존재하지 않습니다. : {}", filePath);
                throw new S3UploadException("삭제 실패 : S3 버킷에 해당 파일이 존재하지 않습니다.");
            }
        }
    }

    private ObjectMetadata createObjectMetadata(MultipartFile multipartFile) { // 업로드한 파일의 유형 설정
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());
        return objectMetadata;
    }

    private void uploadToS3(InputStream inputStream, String keyName, ObjectMetadata metadata) { // S3 에 업로드
        amazonS3.putObject(new PutObjectRequest(bucket, keyName, inputStream, metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    private String generateFileName(String originalFileName) { // UUID 고유 파일명 생성
        String ext = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        String baseName = UUID.randomUUID().toString();
        return baseName + "." + ext;
    }
}
