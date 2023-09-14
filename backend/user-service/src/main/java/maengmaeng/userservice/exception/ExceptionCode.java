package maengmaeng.userservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    /* User */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 사용자가 존재하지 않습니다." ),
    REPOSITORY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자의 레포지토리가 존재하지 않습니다."),
    NICKNAME_ALREADY_IN_USE(HttpStatus.BAD_REQUEST, "해당 닉네임은 이미 사용 중입니다"),
    AVATAR_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 캐릭터를 찾을 수 없습니다."),


    FOLLOW_CANCEL_FAILED(HttpStatus.BAD_REQUEST, "친구삭제를 실행할 수 없습니다."),
    ALREADY_REQUESTED(HttpStatus.CONFLICT, "이미 친구 관계입니다."),
    RELATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "친구 신청 내역이 없습니다.");

    private final HttpStatus status;
    private final String message;
}
