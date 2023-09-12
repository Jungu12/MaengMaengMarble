package maengmaeng.userservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    /* User */
    USER_NOT_FOUND("해당 사용자가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    REPOSITORY_NOT_FOUND
            ("해당 사용자의 레포지토리가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    NICKNAME_ALREADY_IN_USE("해당 닉네임은 이미 사용 중입니다", HttpStatus.BAD_REQUEST),


    FOLLOW_CANCEL_FAILED("친구삭제를 실행할 수 없습니다.", HttpStatus.BAD_REQUEST),
    ALREADY_REQUESTED("이미 팔로우 관계입니다.", HttpStatus.CONFLICT),
    RELATION_NOT_FOUND("친구 신청 내역이 없습니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus status;
}
