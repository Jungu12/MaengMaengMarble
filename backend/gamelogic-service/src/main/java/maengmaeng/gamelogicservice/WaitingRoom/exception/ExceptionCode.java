package maengmaeng.gamelogicservice.waitingRoom.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {
    /* WaitingRoom */
    WAITINGROOM_FULLED(HttpStatus.BAD_REQUEST, "게임인원이 모두 찼습니다.");

    private final HttpStatus status;
    private final String message;
}
