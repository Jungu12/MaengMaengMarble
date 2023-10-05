package maengmaeng.gamelogicservice.gameRoom.service;

import lombok.AllArgsConstructor;
import maengmaeng.gamelogicservice.gameRoom.domain.GameInfo;
import maengmaeng.gamelogicservice.gameRoom.domain.Land;
import maengmaeng.gamelogicservice.gameRoom.domain.Player;
import maengmaeng.gamelogicservice.gameRoom.repository.GameInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class TaxService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final GameRoomService gameRoomService;
    private final GameInfoRepository gameInfoRepository;

    public void pay(String roomCode) {
        // 1. 현재 게임 정보를 다 불러오기
        GameInfo gameInfo = gameRoomService.getInfo(roomCode);

        // 2. 현재 tax칸에 온 사용자 정보 찾기
        String playerNickname = gameInfo.getInfo().getCurrentPlayer();
        int playerIdx = -1;
        Player player = null;
        for(int i = 0 ; i < gameInfo.getPlayers().length ; i++){
            if(gameInfo.getPlayers()[i].getNickname()!=null && gameInfo.getPlayers()[i].getNickname().equals(playerNickname)){
                playerIdx = i;
                player = gameInfo.getPlayers()[i];
                break;
            }
        }


        // 3. 현재 게임 정보에 있는 lands에서 칸에온 사람 땅을 구하기
        // 땅을 담을 리스트 생성
        List<Land> playersLand = new ArrayList<>();
        // owner랑 현재턴인사람 똑같으면 리스트에 추가하기
        for(int i = 0 ; i < gameInfo.getLands().size() ;i++){
            if(gameInfo.getLands().get(i).getOwner()==playerIdx){
                playersLand.add(gameInfo.getLands().get(i));
            }
        }

        // 4. 리스트에 돌면서 땅이 몇갠지 확인하기
        // 이제 내가 가진 땅을 돌면서 땅을 몇개 가졌는지 확인하기
        double taxTotal  = 0;
        for(Land land : playersLand){
            // 일단 나라 한개 !!!
            int buildingTmp = 0;
            double taxPercent = 0;
            boolean[] has;
            has = land.getBuildings();
            int cnt = 0;
            for(int i = 0 ; i < 3; i++){
                if(has[i]){
                    // 몇개가지고 있는지 확인하기
                    cnt++;
                    buildingTmp += land.getCurrentBuildingPrices()[i];
                }
            }
            // 건물이 2개이하일때
            if(cnt<=2){
                if(buildingTmp<=3000000){
                    taxPercent = 0.5;
                }else if(buildingTmp<=6000000){
                    taxPercent = 0.7;
                }else if(buildingTmp<=12000000){
                    taxPercent = 1;
                }else if(buildingTmp<=25000000){
                    taxPercent = 1.3;
                }else if(buildingTmp<=50000000){
                    taxPercent = 1.5;
                }else if(buildingTmp<=94000000){
                    taxPercent = 2.0;
                }else{
                    taxPercent = 2.7;
                }
            }else{
                if(buildingTmp<=3000000){
                    taxPercent = 0.5;
                }else if(buildingTmp<=6000000){
                    taxPercent = 0.7;
                }else if(buildingTmp<=12000000){
                    taxPercent = 1;
                }else if(buildingTmp<=25000000){
                    taxPercent = 2.0;
                }else if(buildingTmp<=50000000){
                    taxPercent = 3.0;
                }else if(buildingTmp<=94000000){
                    taxPercent = 4.0;
                }else{
                    taxPercent = 5.0;
                }
            }

            taxTotal += buildingTmp*(taxPercent*1/100);
        }

        double realtax = taxTotal / gameInfo.getPlayers().length;

        // 7. 해당 세금 모든 사람한테 1/n해주기
        Player[] players = gameInfo.getPlayers();
        for(int i = 0 ; i < players.length ; i++){
            if(players[i] !=null) {
                players[i].setMoney((long) (players[i].getMoney() + realtax));
                players[i].setAsset((long) (players[i].getAsset() + realtax));
            }
        }

        player.setMoney((long) (player.getMoney() - taxTotal));
        player.setAsset((long) (player.getAsset() - taxTotal));

        gameInfoRepository.createGameRoom(gameInfo);
    }
}
