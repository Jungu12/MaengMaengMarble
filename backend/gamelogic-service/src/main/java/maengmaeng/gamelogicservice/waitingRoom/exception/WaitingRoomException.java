package maengmaeng.gamelogicservice.waitingRoom.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class WaitingRoomException extends RuntimeException{
    private final ExceptionCode exceptionCode;

    public String getMessage(){
        return "[WAITINGROOM] " + exceptionCode.getMessage();
    }

    public HttpStatus getHttpStatus() {
        return exceptionCode.getStatus();
    }
}
