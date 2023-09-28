package maengmaeng.userservice.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RelationException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public String getMessage() {
        return exceptionCode.getMessage();
    }

    public HttpStatus getHttpStatus() {
        return exceptionCode.getStatus();
    }
}
