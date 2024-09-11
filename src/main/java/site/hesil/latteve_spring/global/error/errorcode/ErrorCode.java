package site.hesil.latteve_spring.global.error.errorcode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * packageName    : site.hesil.latteve_spring.global.error.errorcode
 * fileName       : ErrorCode
 * author         : Yeong-Huns
 * date           : 2024-08-22
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-22        Yeong-Huns       최초 생성
 * 2024-08-24        yunbin           TOKEN_EXPIRED, ILLEGAL_REGISTRATION_ID 추가
 */
@Getter
public enum ErrorCode {
    // Auth
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "D90", "인증 정보가 없는 요청입니다"),
    TOKEN_INVALID(HttpStatus.BAD_REQUEST, "D91", "유효하지 않은 토큰입니다"),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "D92", "비밀번호와 비밀번호 확인이 일치하지 않습니다."),
    PASSWORD_INVALID(HttpStatus.BAD_REQUEST, "D93", "비밀번호가 올바르지 않습니다."),
    EXISTS_EMAIL(HttpStatus.CONFLICT, "D95", "이미 존재하는 이메일입니다."),
    EXISTS_NICKNAME(HttpStatus.CONFLICT, "D96", "이미 존재하는 닉네임입니다."),
    UNAUTHORIZED_ACTION(HttpStatus.UNAUTHORIZED, "D97", "허가되지 않은 행동입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED,"D98", "토큰이 만료되었습니다."),
    ILLEGAL_REGISTRATION_ID(HttpStatus.NOT_ACCEPTABLE, "D99","illegal registration id"),
    INVALID_JWT_SIGNATURE(HttpStatus.UNAUTHORIZED,"D89","잘못된 JWT 시그니처입니다."),
    ALREADY_APPLICATION(HttpStatus.UNAUTHORIZED, "D88", "이미 지원중입니다."),
    // User
    ALREADY_EXIST(HttpStatus.BAD_REQUEST, "D94", "이미 존재하는 사용자입니다."),

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "D00", "잘못된 구문 요청입니다."),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "D01", "지정한 리소스에 대한 액세스 권한이 없습니다."),
    PAYMENT_REQUIRED(HttpStatus.PAYMENT_REQUIRED, "D02", "리소스에 액세스 하기 위해서는 결제가 필요합니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "D03", "지정한 리소스에 대한 액세스는 금지되었습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "D04", "대상 리소스를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "D05", "허용되지 않은 메소드 호출 : 요청한 URI 가 지정한 메소드를 지원하지 않습니다."),
    NOT_VALID_JSON(HttpStatus.PRECONDITION_FAILED, "D12", "JSON 형식을 기대했지만, JSON 형식으로 변환할 수 없는 값입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E00", "서버 에러가 발생했습니다."),
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "E02", "게이트웨이 또는 프록시 서버가 잘못된 응답을 받았습니다."),
    // -------------------------------- 여기부턴 커스텀 에러 -----------------------------------------
    // 커스텀 예외 생성시 code 90번부터 지정 -> ex) REQUIRE_MORE_COFFEE(HttpStatus.SERVICE_UNAVAILABLE, "E90", "더 많은 커피가 필요합니다.")
    REQUIRE_MORE_COFFEE(HttpStatus.SERVICE_UNAVAILABLE, "E90", "커피가 부족 합니다."),
    S3_IMAGE_UPLOAD_FAIL(HttpStatus.SERVICE_UNAVAILABLE, "E91", "S3 버킷에 이미지를 업로드하는데 실패하였습니다."),
    //BAD_REQUEST(HttpStatus.BAD_REQUEST, "D00", "승인되지 않은 리디렉션 URI입니다.")
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "D92", "해당 작업은 로그인이 필요합니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
