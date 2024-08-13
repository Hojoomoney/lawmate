package site.lawmate.admin.domain.vo;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionStatus {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad Request", 400),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "Invalid Input", 401),

    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Invalid Password", 402),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized", 403),
    ALREADY_EXISTS(HttpStatus.UNAUTHORIZED, "Already Exists", 402),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden", 404),

    NOT_FOUND(HttpStatus.NOT_FOUND, "Not Found", 405),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", 500),

    MONGODB_FIND_ERROR(HttpStatus.BAD_REQUEST, "MongoDB Find Error", 501),
    MONGODB_SAVE_ERROR(HttpStatus.BAD_REQUEST, "MongoDB Save Error", 502),

    KAFKA_SEND_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Kafka Send Error", 503);

    private final HttpStatus status;
    private final String message;
    private final int code;

    ExceptionStatus(HttpStatus status, String message, int code) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
