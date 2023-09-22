package maengmaeng.gamelogicservice.exception;

import maengmaeng.gamelogicservice.exception.dto.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(LobbyException.class)
    public ResponseEntity<ExceptionResponse> handleLobbyException(LobbyException exception) {
        logger.debug("handleLobbyException(), exception status : {}, exception message : {}",
                exception.getHttpStatus(),
                exception.getMessage());
        return ResponseEntity.status(exception.getHttpStatus()).body(new ExceptionResponse(exception.getMessage()));
    }
}
