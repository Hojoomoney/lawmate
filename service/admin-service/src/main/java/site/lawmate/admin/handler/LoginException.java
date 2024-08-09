package site.lawmate.admin.handler;

import lombok.Getter;
import site.lawmate.admin.domain.vo.ExceptionStatus;

@Getter

public class LoginException extends RuntimeException{
    private final ExceptionStatus status;

    public LoginException(ExceptionStatus status) {
        super(status.getMessage());
        this.status = status;
    }

    public LoginException(ExceptionStatus status, String message) {
        super(status.getMessage() + " : " + message);
        this.status = status;
    }
}
