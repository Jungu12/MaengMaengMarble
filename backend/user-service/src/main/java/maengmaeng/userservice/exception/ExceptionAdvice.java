package maengmaeng.userservice.exception;


import maengmaeng.userservice.exception.dto.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ExceptionResponse> handleUserException(UserException exception) {
        logger.debug("handleUserException(), exception status : {}, exception message : {}",
                exception.getHttpStatus(),
                exception.getMessage());
        return ResponseEntity.status(exception.getHttpStatus()).body(new ExceptionResponse(exception.getMessage()));
    }


    @ExceptionHandler(RelationException.class)
    public ResponseEntity<ExceptionResponse> handleRelationException(RelationException exception) {
        logger.debug("handleRelationException(), exception status : {}, exception message: {}",
                exception.getHttpStatus(),
                exception.getMessage());
        return ResponseEntity.status(exception.getHttpStatus()).body(new ExceptionResponse(exception.getMessage()));
    }

}
