package site.hesil.latteve_spring.global.error.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import site.hesil.latteve_spring.global.error.errorcode.ErrorCode;

/**
 * packageName    : site.hesil.latteve_spring.global.error.response
 * fileName       : ErrorResponse
 * author         : Yeong-Huns
 * date           : 2024-08-22
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-22        Yeong-Huns       최초 생성
 */
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    private String Message;
    private String code;

    public ErrorResponse(final ErrorCode code) {
        this.Message = code.getMessage();
        this.code = code.getCode();
    }

    public ErrorResponse(final ErrorCode code, final String message) {
        this.Message = message;
        this.code = code.getCode();
    }

    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(final ErrorCode code, final String message) {
        return new ErrorResponse(code, message);
    }
}
