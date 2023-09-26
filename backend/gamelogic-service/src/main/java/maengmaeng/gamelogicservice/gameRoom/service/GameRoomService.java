package maengmaeng.gamelogicservice.gameRoom.service;

import lombok.AllArgsConstructor;
import maengmaeng.gamelogicservice.gameRoom.domain.*;
import maengmaeng.gamelogicservice.gameRoom.domain.db.*;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.GameStart;
import maengmaeng.gamelogicservice.gameRoom.repository.*;
import maengmaeng.gamelogicservice.waitingRoom.domain.dto.UserInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GameRoomService {

    private final DbCountryRespository dbCountryRespository;
    private final DbNewsRepository dbNewsRepository;
    private final DbStockRepository dbStockRepository;
    private final DbNewsCountryRepository dbNewsCountryRepository;
    private final DbCardRepository dbCardRepository;
    private final DbNewsStockRepository dbNewsStockRepository;
    private final GameInfoRepository gameInfoRepository;
    private final GameInfoMapper gameInfoMapper;
    /** 나라 목록 가져오기
     * */
    public GameInfo getInfo (String roomCode) {
        return gameInfoRepository.getGameInfo(roomCode);

    }

    /**
     * 처음 시작 카드 선택
     * */
    public GameStart setStart(String roomCode, int playerCnt){
        int[] cards = {1,2,3,4};
        Random random = new Random();

        // 배열을 뒤에서부터 앞으로 순회하면서 무작위로 요소를 선택하여 섞음
        for (int i = cards.length - 1; i > 0; i--) {
            int randomIndex = random.nextInt(i + 1);

            // 요소 교환
            int temp = cards[i];
            cards[i] = cards[randomIndex];
            cards[randomIndex] = temp;
        }
        List<DbCountry> dbCountryList = dbCountryRespository.findAll();
        int platinum = dbNewsRepository.findByNewsType("Platinum").size();
        int silver = dbNewsRepository.findByNewsType("silver").size();
        int bronze = dbNewsRepository.findByNewsType("Bronze").size();
        List<Land> landList = dbCountryList.stream().map(gameInfoMapper::toRedisLand).collect(Collectors.toList());
        List<Stock> stockList = dbStockRepository.findAll().stream().map(gameInfoMapper::toRedisStock).collect(Collectors.toList());

        GameInfo gameInfo = GameInfo.builder()
                .roomCode(roomCode)
                .players(new Player[4])
                .lands(landList)
                .goldenKeys(gameInfoMapper.toRedisGoldenKeys(bronze,silver,platinum))
                .stocks(stockList)
                .build();

        gameInfoRepository.createGameRoom(gameInfo);


        return GameStart.builder().cards(cards).build();

    }

    /**
     * 플레이어 게임 순서 세팅*/
    public Player setPlayer(String roomCode, UserInfo userInfo, int playerCnt){

        GameInfo gameInfo = getInfo(roomCode);
        Player[] players = gameInfo.getPlayers();
        Player player = gameInfoMapper.toReidsPlayer(userInfo.getUserId(), userInfo.getNickname());

        return  player;
    }


    /**/
    public GameInfo setInfo (String roomCode){
        Player[] players = new Player[4];

        List<DbCountry> dbCountryList = dbCountryRespository.findAll();
        int platinum = dbNewsRepository.findByNewsType("Platinum").size();
        int silver = dbNewsRepository.findByNewsType("silver").size();
        int bronze = dbNewsRepository.findByNewsType("Bronze").size();

        List<Land> landList = dbCountryList.stream().map(gameInfoMapper::toRedisLand).collect(Collectors.toList());

        List<Stock> stockList = dbStockRepository.findAll().stream().map(gameInfoMapper::toRedisStock).collect(Collectors.toList());
        int[] news = new int[3];
        GameInfo gameInfo = GameInfo.builder()
                .roomCode(roomCode)
                .players(players)
                .lands(landList)
                .goldenKeys(gameInfoMapper.toRedisGoldenKeys(bronze,silver,platinum))
                .stocks(stockList)
                .build();




        return gameInfoRepository.createGameRoom(gameInfo);


    }
    /**
     * 주사위 굴리기.
    * */
    public static void rollDice(){
        Random random = new Random();

        // 주사위 1 던지기 (1부터 6까지)
        int dice1 = random.nextInt(6) + 1;

        // 주사위 2 던지기 (1부터 6까지)
        int dice2 = random.nextInt(6) + 1;

        System.out.println("첫 번째 주사위: " + dice1);
        System.out.println("두 번째 주사위: " + dice2);

    }
    /**
     * 턴을 종료하는 로직
     * 1. 게임 정보를 가져오기
    * */
//    public GameInfo endTurn(String roomCode){
//
//        GameInfo gameInfo =gameInfoRepository.getGameInfo(roomCode);
//        // player 리스트
//
//        Player[] players= gameInfo.getPlayers();
//        Info info = gameInfo.getInfo();
//        String currentPlayer = info.getCurrentPlayer();
//        System.out.println(currentPlayer);
//        int playerIdx =-1;
//        boolean nextTurn = false;
//        // 현재 플레이어
//        for(int i=0;i<players.length;i++){
//            if(players[i].getNickname().equals(currentPlayer)){
//                playerIdx = i;
//            }
//        }
//        if(playerIdx==-1){
//            System.out.println("혼자 살아있음");
//        }
//        if(playerIdx==3){
//            // 턴 카운트를 +1
//            System.out.println("마지막 플레이어");
//            nextTurn = true;
//        }
//        // 살아있는 마지막 플레이어
//        for(int i=0;i<3;i++){
//            int nextPlayerIdx = playerIdx+1;
//
//            // 플레이어가 살아있
//            if(players[nextPlayerIdx+i].isAlive()){
//
//                break;
//            }
//        }
//
//
//
//
//        return gameInfo;
//
//
//
//
//    }


//    public  void shuffleArray(Player[] array) {
//        int index, n = array.length;
//        Random random = new Random();
//        for (int i = n - 1; i > 0; i--) {
//            index = random.nextInt(i + 1);
//            // 배열의 요소를 무작위로 섞기 위해 요소 위치 교환
//            Player temp = array[index];
//            array[index] = array[i];
//            array[i] = temp;
//        }
//
//    }
//


}
