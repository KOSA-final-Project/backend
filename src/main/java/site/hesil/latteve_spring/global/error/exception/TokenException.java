package site.hesil.latteve_spring.global.error.exception;

import site.hesil.latteve_spring.global.error.errorcode.ErrorCode;

/**
 * packageName    : site.hesil.latteve_spring.global.error.exception
 * fileName       : TokenException
 * author         : yunbin
 * date           : 2024-08-29
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-23           yunbin           최초 생성
 */
public class TokenException extends CustomBaseException {

    public TokenException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TokenException(String message) {
        super(message, ErrorCode.TOKEN_INVALID);
    }
}