package site.hesil.latteve_spring.global.error.exception;

import site.hesil.latteve_spring.global.error.errorcode.ErrorCode;

/**
 * packageName    : site.hesil.latteve_spring.global.error.exception
 * fileName       : BadRequestException
 * author         : yunbin
 * date           : 2024-08-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-27           yunbin           최초 생성
 */

public class BadRequestException extends CustomBaseException{

    public BadRequestException() {
        super(ErrorCode.INVALID_INPUT_VALUE);
    }
    public BadRequestException(String message){
        super(message, ErrorCode.INVALID_INPUT_VALUE);
    }
}
