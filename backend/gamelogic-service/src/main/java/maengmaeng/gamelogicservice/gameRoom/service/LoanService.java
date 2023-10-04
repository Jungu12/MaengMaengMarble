package maengmaeng.gamelogicservice.gameRoom.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import maengmaeng.gamelogicservice.exception.ExceptionCode;
import maengmaeng.gamelogicservice.exception.LoanException;
import maengmaeng.gamelogicservice.gameRoom.domain.GameInfo;
import maengmaeng.gamelogicservice.gameRoom.domain.Player;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.LoanRequest;
import maengmaeng.gamelogicservice.gameRoom.repository.GameInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoanService {
    private final GameRoomService gameRoomService;
    private final GameInfoRepository gameInfoRepository;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void borrow(String roomCode, LoanRequest loanRequest) {
        GameInfo gameInfo = gameRoomService.getInfo(roomCode);

        // 1. 빌린 금액이 3000만원 이상이면 오류 넘기기
        long borrowMoney = loanRequest.getPrice();
        if (borrowMoney >= 30000000) {
            throw new LoanException(ExceptionCode.LOAN_MONEY_OVER);
        }

        // 2. 로그인된 사용자 찾기
        for (Player player : gameInfo.getPlayers()) {
            if(player==null || player.getNickname()==null)continue;
            if (player.getNickname() != null && player.getNickname().equals(gameInfo.getInfo().getCurrentPlayer())) {
                player.setLoan(player.getLoan() + loanRequest.getPrice());
                player.setMoney(player.getMoney() + loanRequest.getPrice());
                player.setAsset(player.getAsset() + loanRequest.getPrice());
                break;
            }
        }


        gameInfoRepository.createGameRoom(gameInfo);
    }

    public void repay(String roomCode, LoanRequest loanRequest) {
        GameInfo gameInfo = gameRoomService.getInfo(roomCode);

        // 1. 로그인된 사용자 찾아서 돈 빼기
        // 소유하고 있는 돈에서 갚을 돈 빼기
        for (Player player : gameInfo.getPlayers()) {
            if (player.getNickname().equals(gameInfo.getInfo().getCurrentPlayer())) {
                // 사용자가 지금 현금으로 갚을 수 있는 상태인지 확인
                if (player.getMoney() >= loanRequest.getPrice()) {
                    player.setLoan(player.getLoan() - loanRequest.getPrice());
                    player.setMoney(player.getMoney() - loanRequest.getPrice());
                    player.setAsset(player.getAsset() - loanRequest.getPrice());
                    break;
                }else{
                    throw new LoanException(ExceptionCode.LOAN_MONEY_SUFFICIENT);
                }
            }
        }

        gameInfoRepository.createGameRoom(gameInfo);
    }
}
