package site.hesil.latteve_spring.global.error.exception;

import site.hesil.latteve_spring.global.error.errorcode.ErrorCode;

/**
 * packageName    : site.hesil.latteve_spring.global.error.exception
 * fileName       : S3UploadException
 * author         : Yeong-Huns
 * date           : 2024-08-24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-24        Yeong-Huns       최초 생성
 */
public class S3UploadException extends CustomBaseException {
    public S3UploadException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }

    public S3UploadException() {
        super(ErrorCode.S3_IMAGE_UPLOAD_FAIL);
    }

    public S3UploadException(String message) {
        super(message, ErrorCode.S3_IMAGE_UPLOAD_FAIL);
    }

    public S3UploadException(String message, Throwable cause) {
        super(message, cause, ErrorCode.S3_IMAGE_UPLOAD_FAIL);
    }

    public S3UploadException(ErrorCode errorCode,Throwable cause) {
        super(errorCode, cause);
    }
}
