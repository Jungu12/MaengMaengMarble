package maengmaeng.userservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    /* User */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자가 존재하지 않습니다."),
    REPOSITORY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자의 레포지토리가 존재하지 않습니다."),
    NICKNAME_ALREADY_IN_USE(HttpStatus.BAD_REQUEST, "해당 닉네임은 이미 사용 중입니다");

    private final HttpStatus status;
    private final String message;
}
