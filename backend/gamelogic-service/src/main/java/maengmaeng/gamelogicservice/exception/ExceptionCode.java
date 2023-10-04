package maengmaeng.gamelogicservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {
    /* Lobby */
    LOBBY_ENTER_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "로비 접근에 실패했습니다."),

    /* Loan */
    LOAN_MONEY_OVER(HttpStatus.INTERNAL_SERVER_ERROR,"3000만원 이상 대출할 수 없습니다."),

    /* Stock */
    MONEY_NOT_SUFFICIENT(HttpStatus.INTERNAL_SERVER_ERROR, "보유 현금이 부족합니다."),
    STOCK_NOT_SUFFICIENT(HttpStatus.INTERNAL_SERVER_ERROR, "보유 주식이 부족합니다."),

    /* PackJinHo */
    PACKJINHO_MONEY_OVER(HttpStatus.INTERNAL_SERVER_ERROR, "보유 현금보다 많은 금액입니다.");

    private final HttpStatus status;
    private final String message;
}
