package maengmaeng.userservice.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class RelationException extends RuntimeException{
    private final ExceptionCode exceptionCode;

    public String getMessage() {
        return "[Relation] " + exceptionCode.getMessage();
    }

    public HttpStatus getHttpStatus() {
        return exceptionCode.getStatus();
    }
}