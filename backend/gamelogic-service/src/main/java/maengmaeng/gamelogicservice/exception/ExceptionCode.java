package maengmaeng.gamelogicservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {
    /* Lobby */
    LOBBY_ENTER_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "로비 접근에 실패했습니다.");

    private final HttpStatus status;
    private final String message;
}
