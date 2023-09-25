package maengmaeng.gamelogicservice.gameRoom.service;

import lombok.AllArgsConstructor;
import maengmaeng.gamelogicservice.gameRoom.domain.*;
import maengmaeng.gamelogicservice.gameRoom.domain.db.*;
import maengmaeng.gamelogicservice.gameRoom.repository.*;
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
    public void getInfo () {

    }



    public GameInfo setInfo (String roomCode){
        Player[] players = new Player[4];
        players[0] = gameInfoMapper.toReidsPlayer("LEE","LEE");
        players[1] = gameInfoMapper.toReidsPlayer("KIM", "KIM");
        players[2] = gameInfoMapper.toReidsPlayer("jungu", "jungu");
        players[3] = gameInfoMapper.toReidsPlayer("215","215");


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
                .info(gameInfoMapper.toRedisInfo(players[0].getNickname(),news,0))
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


    public  void shuffleArray(Player[] array) {
        int index, n = array.length;
        Random random = new Random();
        for (int i = n - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            // 배열의 요소를 무작위로 섞기 위해 요소 위치 교환
            Player temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }

    }



}
