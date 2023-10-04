package maengmaeng.gamelogicservice.gameRoom.service;

import lombok.AllArgsConstructor;
import maengmaeng.gamelogicservice.exception.ExceptionCode;
import maengmaeng.gamelogicservice.exception.StockException;
import maengmaeng.gamelogicservice.gameRoom.domain.GameInfo;
import maengmaeng.gamelogicservice.gameRoom.domain.Player;
import maengmaeng.gamelogicservice.gameRoom.domain.Stock;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.PlayerSeq;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.StockRequest;
import maengmaeng.gamelogicservice.gameRoom.repository.GameInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StockService {
    private final GameRoomService gameRoomService;
    private final GameInfoRepository gameInfoRepository;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void purchase(String roomCode, StockRequest stockRequest) {
        // 1. 플레이어가 사려는 stock의 가격을 책정하기 : stock의 현재가격 * 몇 주
        logger.info("purchase()");

        String stockName = stockRequest.getName();
        int stockCnt = stockRequest.getCnt();

        GameInfo gameInfo = gameRoomService.getInfo(roomCode);
        int stockPrice = 0;
        int stockId = -1;
        Player nowPlayer = null;

        for (Stock stock : gameInfo.getStocks()) {
            if (stock.getName().equals(stockName)) {
                stockPrice = stock.getCurrentCost();
                stockId = stock.getId();
                break;
            }
        }
        stockPrice = stockPrice * stockCnt;

        // 2. 플레이어가 가진 현금찾기
        long playerMoney = 0;
        for (Player player : gameInfo.getPlayers()) {
            if (player.getNickname().equals(gameInfo.getInfo().getCurrentPlayer())) {
            if (player!=null && player.getNickname().equals(playerSeq.getNickname())) {
                playerMoney = player.getMoney();
                nowPlayer = player;
                break;
            }
        }

        // 3. 못사면 못산다고 메세지 넘기기
        if (playerMoney < stockPrice) {
            throw new StockException(ExceptionCode.MONEY_NOT_SUFFICIENT);
        }

        // 4. 플레이어가 구매 가능한 경우 : 플레이어돈 >= 주식가격
        if (playerMoney >= stockPrice) {
            nowPlayer.setMoney(nowPlayer.getMoney() - stockPrice);
            nowPlayer.setAsset(nowPlayer.getAsset() - stockPrice);

            // 플레이어의 주식 목록에 추가하기
            int[] newStocks = new int[nowPlayer.getStocks().length];

            for (int i = 0; i < nowPlayer.getStocks().length; i++) {
                if (i == stockId) {
                    newStocks[i] = nowPlayer.getStocks()[i] + stockCnt;
                } else {
                    newStocks[i] = nowPlayer.getStocks()[i];
                }
            }
            // 새로운 주식목록으로 업데이트하기
            nowPlayer.setStocks(newStocks);
        }


        // 변경된 정보 다시 저장
        gameInfoRepository.createGameRoom(gameInfo);


    }

    public void sell(String roomCode, StockRequest stockRequest) {
        // 1. 현재 게임방 정보 불러오기

        GameInfo gameInfo = gameRoomService.getInfo(roomCode);
        Player nowPlayer = null;
        int stockId = -1;
        int stockPrice = 0;

        // 2. 플레이어 찾기
        for (Player player : gameInfo.getPlayers()) {
            if (player.getNickname().equals(gameInfo.getInfo().getCurrentPlayer())) {
                nowPlayer = player;
                break;
            }
        }

        // 팔려는 주식의 인덱스와 현재가격 찾기
        for (Stock stock : gameInfo.getStocks()) {
            if (stock.getName().equals(stockRequest.getName())) {
                stockPrice = stock.getCurrentCost();
                stockId = stock.getId();
            }
        }

        // 3. 팔려는 주식이 현재 플레이어가 가지고 있는 주식인지 확인하기
        if(nowPlayer.getStocks()[stockId] < stockRequest.getCnt()){
            throw new StockException(ExceptionCode.STOCK_NOT_SUFFICIENT);
        }


        // 3. 가지고 있으면 판매하기
        int[] newStocks = new int[nowPlayer.getStocks().length];
        for(int i = 0 ; i < nowPlayer.getStocks().length; i++){
            if(i==stockId){
                newStocks[i] = nowPlayer.getStocks()[i] - stockRequest.getCnt();
            }else {
                newStocks[i] = nowPlayer.getStocks()[i];
            }
        }
        nowPlayer.setStocks(newStocks);
        nowPlayer.setMoney(nowPlayer.getMoney() + stockPrice*stockRequest.getCnt());
        nowPlayer.setAsset(nowPlayer.getAsset() + stockPrice*stockRequest.getCnt());
        nowPlayer.setMoney(nowPlayer.getMoney() + stockPrice*stockInfo.getCnt());
        nowPlayer.setAsset(nowPlayer.getAsset() + stockPrice*stockInfo.getCnt());

        // 변경된 정보 다시 저장
        gameInfoRepository.createGameRoom(gameInfo);
    }
}
