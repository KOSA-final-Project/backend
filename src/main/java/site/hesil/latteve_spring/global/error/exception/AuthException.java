package site.hesil.latteve_spring.global.error.exception;

import site.hesil.latteve_spring.global.error.errorcode.ErrorCode;

/**
 * packageName    : site.hesil.latteve_spring.global.error.exception
 * fileName       : AuthException
 * author         : yunbin
 * date           : 2024-08-29
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-29           yunbin           최초 생성
 */
public class AuthException extends CustomBaseException {

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
