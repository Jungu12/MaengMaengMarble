package maengmaeng.gamelogicservice.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

import java.net.http.HttpHeaders;

@AllArgsConstructor
public class LobbyException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public String getMessage() {
        return "[Lobby]" + exceptionCode.getMessage();
    }

    public HttpStatus getHttpStatus() {
        return exceptionCode.getStatus();
    }
}
