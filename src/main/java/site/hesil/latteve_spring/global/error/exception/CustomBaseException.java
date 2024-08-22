package site.hesil.latteve_spring.global.error.exception;

import lombok.Getter;
import site.hesil.latteve_spring.global.error.errorcode.ErrorCode;

/**
 * packageName    : site.hesil.latteve_spring.global.error.exception
 * fileName       : CustomBaseException
 * author         : Yeong-Huns
 * date           : 2024-08-22
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-22        Yeong-Huns       최초 생성
 */
@Getter
public class CustomBaseException extends RuntimeException{
    private final ErrorCode responseCode;

    public CustomBaseException(String message, ErrorCode errorCode) {
        super(message);
        this.responseCode = errorCode;
    }

    public CustomBaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.responseCode = errorCode;
    }

    public CustomBaseException(String message) {
        super(message);
        this.responseCode = ErrorCode.INTERNAL_SERVER_ERROR;
    }
}