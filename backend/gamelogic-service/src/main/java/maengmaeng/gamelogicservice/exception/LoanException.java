package maengmaeng.gamelogicservice.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class LoanException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public String getMessage() {
        return "[Loan]" + exceptionCode.getMessage();
    }

    public HttpStatus getHttpStatus() {
        return exceptionCode.getStatus();
    }
}
