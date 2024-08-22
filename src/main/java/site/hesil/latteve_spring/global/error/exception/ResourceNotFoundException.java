package site.hesil.latteve_spring.global.error.exception;

import site.hesil.latteve_spring.global.error.errorcode.ErrorCode;

/**
 * packageName    : site.hesil.latteve_spring.global.error.exception
 * fileName       : ResourceNotFoundException
 * author         : Yeong-Huns
 * date           : 2024-08-22
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-22        Yeong-Huns       최초 생성
 */
public class ResourceNotFoundException extends NotFoundException{
    public ResourceNotFoundException() {
        super(ErrorCode.NOT_FOUND);
    }
    public ResourceNotFoundException(String message) {
        super(message);
    }
    public ResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
