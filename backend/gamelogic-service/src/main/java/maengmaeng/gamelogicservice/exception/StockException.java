package maengmaeng.gamelogicservice.exception;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class StockException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public String getMessage() {
        return "[Stock]" + exceptionCode.getMessage();
    }

    public HttpStatus getHttpStatus() {
        return exceptionCode.getStatus();
    }
}