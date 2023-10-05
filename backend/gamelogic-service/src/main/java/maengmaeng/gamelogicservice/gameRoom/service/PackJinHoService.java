package maengmaeng.gamelogicservice.gameRoom.service;

import lombok.AllArgsConstructor;
import maengmaeng.gamelogicservice.exception.ExceptionCode;
import maengmaeng.gamelogicservice.exception.PackJinHoException;
import maengmaeng.gamelogicservice.gameRoom.domain.GameInfo;
import maengmaeng.gamelogicservice.gameRoom.domain.Player;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.PackJinHoRequest;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.ParkJinHoResponse;
import maengmaeng.gamelogicservice.gameRoom.repository.GameInfoRepository;
import maengmaeng.gamelogicservice.global.dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@AllArgsConstructor
public class PackJinHoService {
    private final GameRoomService gameRoomService;
    private final GameInfoRepository gameInfoRepository;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public ResponseDto betting(String roomCode, PackJinHoRequest packJinHoRequest) {
        Random random = new Random();
        // 게임 정보 가져오기
        GameInfo gameInfo = gameRoomService.getInfo(roomCode);
        Player[] players = gameInfo.getPlayers();

        String currentPlayer = gameInfo.getInfo().getCurrentPlayer();
        int currentIdx = -1;
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null && players[i].isAlive() && players[i].getNickname().equals(currentPlayer)) {
                currentIdx = i;
            }
        }
        if (currentIdx != -1) {
            // 예외 처리
        }
        // 현재 플레이어의 보유 현금
        long currentMoney = players[currentIdx].getMoney();
        // 현재 플레이어가 배팅하려고 하는 금액
        int bettingMoney = packJinHoRequest.getBettingMoney();
        // 1. 배팅할 금액이 보유 현금 이상이면 오류 넘기기
        if (bettingMoney > currentMoney) {
            throw new PackJinHoException(ExceptionCode.PACKJINHO_MONEY_OVER);
        }

        // 2. 박진호 경우의 수
        int num1 = random.nextInt(10);
        int num2 = random.nextInt(10);
        int num3 = random.nextInt(10);
        if (num1 == num2 && num2 == num3) {
            int winnings = bettingMoney * 49;
            players[currentIdx].setMoney(players[currentIdx].getMoney() + winnings);
            players[currentIdx].setAsset(players[currentIdx].getAsset() + winnings);
        }
        else if (num1 == 7 && num2 == 7 && num3 == 7) {
            return ResponseDto.builder().type("게임종료").build();
        }
        else if (num1 == num2 || num2 == num3 || num3 == num1) {
            players[currentIdx].setMoney(players[currentIdx].getMoney() + bettingMoney);
            players[currentIdx].setAsset(players[currentIdx].getAsset() + bettingMoney);
        }
        else {
            players[currentIdx].setMoney(players[currentIdx].getMoney() - bettingMoney);
            players[currentIdx].setAsset(players[currentIdx].getAsset() - bettingMoney);
        }

        // 바뀐 정보 gameInfo에 업데이트
        gameInfo.setPlayers(players);

        gameInfoRepository.createGameRoom(gameInfo);

        return ResponseDto.builder().type("박진호 끝").data(ParkJinHoResponse.builder().players(players).num(new int[] {num1, num2, num3}).build()).build();
    }
}
