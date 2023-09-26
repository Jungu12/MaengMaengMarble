package maengmaeng.gamelogicservice.gameRoom.service;

import lombok.AllArgsConstructor;
import maengmaeng.gamelogicservice.gameRoom.domain.*;
import maengmaeng.gamelogicservice.gameRoom.domain.db.*;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.Dice;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.GameStart;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.PlayerSeq;
import maengmaeng.gamelogicservice.gameRoom.repository.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
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

        int[] cards = new int[playerCnt];
        for(int i=0;i<playerCnt;i++){
            cards[i] = i+1;
        }
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

        List<News> platinumNews = dbNewsRepository.findByNewsType("Platinum").stream().map(gameInfoMapper::toRedisNews).collect(Collectors.toList());
        Collections.shuffle(platinumNews);
        List<News> diamondNews = dbNewsRepository.findByNewsType("Diamond").stream().map(gameInfoMapper::toRedisNews).collect(Collectors.toList());
        Collections.shuffle(diamondNews);
        List<News> bronzeNews = dbNewsRepository.findByNewsType("Bronze").stream().map(gameInfoMapper::toRedisNews).collect(Collectors.toList());
        Collections.shuffle(bronzeNews);
        int bronze = bronzeNews.size();
        int diamond = diamondNews.size();
        int platinum = platinumNews.size();
        List<Land> landList = dbCountryList.stream().map(gameInfoMapper::toRedisLand).collect(Collectors.toList());
        List<Stock> stockList = dbStockRepository.findAll().stream().map(gameInfoMapper::toRedisStock).collect(Collectors.toList());

        GameInfo gameInfo = GameInfo.builder()
                .roomCode(roomCode)
                .players(new Player[4])
                .lands(landList)
                .info(Info.builder().playerCnt(playerCnt).build())
                .goldenKeys(gameInfoMapper.toRedisGoldenKeys(bronze,diamond,platinum))
                .stocks(stockList)
                .newsInfo(NewsInfo.builder().bronze(bronzeNews).diamond(diamondNews).platinum(platinumNews).build())
                .build();

        gameInfoRepository.createGameRoom(gameInfo);


        return GameStart.builder().cards(cards).build();

    }

    /**
     * 플레이어 게임 순서 세팅*/
    public Player[] setPlayer(String roomCode, PlayerSeq playerSeq){

        GameInfo gameInfo = getInfo(roomCode);
        Player[] players = gameInfo.getPlayers();
        int playerNum = gameInfo.getInfo().getPlayerCnt();

        Player player = gameInfoMapper.toReidsPlayer(playerSeq.getUserId(), playerSeq.getNickname());
        if(players[playerSeq.getPlayerCnt()-1] ==null){
            players[playerSeq.getPlayerCnt()-1] = player;
            if(playerSeq.getPlayerCnt()==1){
                Info info = Info.builder()
                        .currentPlayer(players[0].getNickname())
                        .playerCnt(playerNum)
                        .turnCount(1)
                        .effectNews(new LinkedList<>())
                        .waitingNews(new LinkedList<WaitingNews>())
                        .build();
                gameInfo.setInfo(info);
            }
        }

        gameInfoRepository.createGameRoom(gameInfo);
        return  players;
    }


    /**/
//    public GameInfo setInfo (String roomCode){
//        Player[] players = new Player[4];
//
//        List<DbCountry> dbCountryList = dbCountryRespository.findAll();
//        int platinum = dbNewsRepository.findByNewsType("Platinum").size();
//        int silver = dbNewsRepository.findByNewsType("silver").size();
//        int bronze = dbNewsRepository.findByNewsType("Bronze").size();
//
//        List<Land> landList = dbCountryList.stream().map(gameInfoMapper::toRedisLand).collect(Collectors.toList());
//
//        List<Stock> stockList = dbStockRepository.findAll().stream().map(gameInfoMapper::toRedisStock).collect(Collectors.toList());
//        int[] news = new int[3];
//        GameInfo gameInfo = GameInfo.builder()
//                .roomCode(roomCode)
//                .players(players)
//                .lands(landList)
//                .goldenKeys(gameInfoMapper.toRedisGoldenKeys(bronze,silver,platinum))
//                .stocks(stockList)
//                .build();
//
//
//
//
//        return gameInfoRepository.createGameRoom(gameInfo);
//
//
//    }
    /**
     * 주사위 굴리기.
    * */
    public Dice rollDice(String roomCode){
        // 게임 정보 가져오기
        GameInfo gameInfo = gameInfoRepository.getGameInfo(roomCode);
        Player[] players = gameInfo.getPlayers();

        String currentPlayer = gameInfo.getInfo().getCurrentPlayer();
        int currentIdx =-1;
        for(int i=0;i< players.length;i++){
            if(players[i]!=null && players[i].isAlive() && players[i].getNickname().equals(currentPlayer)){
                currentIdx = i;
            }
        }
//        if(currentIdx != -1){
//
//        }

        Random random = new Random();

        // 주사위 1 던지기 (1부터 6까지)
        int dice1 = random.nextInt(6) + 1;

        // 주사위 2 던지기 (1부터 6까지)
        int dice2 = random.nextInt(6) + 1;

        Dice dice = new Dice();
        dice.setDice1(dice1);
        dice.setDice2(dice2);
        dice.setDoubleCount(0);
        // 더블 일 때
//        if(dice1 == dice2){
//
//        }
        System.out.println(dice1 + " " + dice2);
        return dice;
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





}
