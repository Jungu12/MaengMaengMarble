package maengmaeng.gamelogicservice.gameRoom.service;

import lombok.AllArgsConstructor;
import maengmaeng.gamelogicservice.gameRoom.domain.*;
import maengmaeng.gamelogicservice.gameRoom.domain.db.*;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.Dice;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.GameStart;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.PlayerSeq;
import maengmaeng.gamelogicservice.gameRoom.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;
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
    private static final int stopTrade = 8;
    /**
     * 게임 정보  가져오기
     * Params: roomCode
     * return GameInfo
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

    /**
     * 현재 플레이어 인덱스 가져오기
     *
     * */
    public int getPlayerIdx(String roomCode, String currentPlayer){
        GameInfo gameInfo = gameInfoRepository.getGameInfo(roomCode);
        Player[] players = gameInfo.getPlayers();
        int currentIdx=-1;
        for(int i=0;i< players.length;i++){
            if(players[i]!=null && players[i].isAlive() && players[i].getNickname().equals(currentPlayer)){
                currentIdx = i;
            }
        }
        return currentIdx;
    }
    /**
     * 주사위 눈 반환
     * */
    public Dice getDice(){
        Random random = new Random();

        // 주사위 1 던지기 (1부터 6까지)
        int dice1 = random.nextInt(6) + 1;

        // 주사위 2 던지기 (1부터 6까지)
        int dice2 = random.nextInt(6) + 1;

        return Dice.builder().dice1(dice1).dice2(2).build();
    }

    /**
     * 주사위 굴리기.
    * */
    public Dice rollDice(String roomCode){
        // 게임 정보 가져오기
        GameInfo gameInfo = gameInfoRepository.getGameInfo(roomCode);
        Player[] players = gameInfo.getPlayers();

        String currentPlayer = gameInfo.getInfo().getCurrentPlayer();
        int currentIdx =-1;
        // 현재 플레이어 인덱스 찾기
        for(int i=0;i< players.length;i++){
            if(players[i]!=null && players[i].isAlive() && players[i].getNickname().equals(currentPlayer)){
                currentIdx = i;
            }
        }
        if(currentIdx != -1){
            // 예외 처리
        }
        // 주사위 굴리기
        Dice dice = getDice();
        Player curPlayer = players[currentIdx];

        boolean checkTrade = false;
        // 더블 일 때
        if(dice.getDice1() == dice.getDice2()) {
            int doubleCount = curPlayer.getDoubleCount();
            doubleCount++;
            // 더블이 3번 나오면 거래정지 칸으로 이동 및 턴 종료
            if (doubleCount >= 3) {
                checkTrade = true;
                //TODO:

            }
            curPlayer.setDoubleCount(doubleCount);

        }

        if(checkTrade){
            // 거래 정지 칸으로 이동(한 바퀴 돌아서 거래정지로 가는 걸까??)
            curPlayer.setCurrentLocation(stopTrade);

        } else{
            Player player = move(curPlayer, dice.getDice1()+dice.getDice2());

        }


        //TODO: 플레이어 관련 정보 REDIS에 다시 저장.
        // 플레이어 정보 재설정
        players[currentIdx] =  curPlayer;
        gameInfo.setPlayers(players);
        gameInfoRepository.createGameRoom(gameInfo);
        dice.setDoubleCount(curPlayer.getDoubleCount());



        return dice;
    }


    /**
     *  현재 칸이 거래 정지에 있고 player의 doubleCount가 3이 아닐 때 주사위 굴리기
     * */
    public GameInfo stopTrade(String roomCode){
        GameInfo gameInfo = getInfo(roomCode);
        // 더블이면 탈출 or stopTradeCount 값이 3이면 탈출
        Player[] players = gameInfo.getPlayers();

        String currentPlayer = gameInfo.getInfo().getCurrentPlayer();
        int currentIdx =-1;
        // 현재 플레이어 인덱스 찾기
        for(int i=0;i< players.length;i++){
            if(players[i]!=null && players[i].isAlive() && players[i].getNickname().equals(currentPlayer)){
                currentIdx = i;
            }
        }
        Player curPlayer = players[currentIdx];
        // 현재 위치가 stopTrade 위치가 아니면
        if(curPlayer.getCurrentLocation()!=stopTrade){
            // 예외 처리
            System.out.println("예외");
        }

        // 주사위 던지기
        Dice dice = getDice();

        // TODO:  더블이 아니면 턴 종료
        if(dice.getDice1() != dice.getDice2()){

//            return gameInfo;
        }
        // TODO: 더블이면 이동
        Player player = move(curPlayer,dice.getDice1()+ dice.getDice2());



        return gameInfo;
    }
    /**
     * 맹맹 지급
     * */
//    public Player manegMaeng(Player player){
//        // 맹맹: 보유 현금 * 0.15 + 배당금 - 대출 원금 * 0.24)
//        long money = Math.round(player.getMoney() * 1.15);
//        List<Map<Integer,Integer>> stock = player.getStocks();
//        long dividends =
//        Long maengManeg =
//
//    }

    /**
     * 총자산 계산
     * */
//    public Long calculateAsset(Player player){
//        //TODO: asset 계산
//
//
//    }


    /**
     * 이동 로직
     * */
    public Player move(Player player, int move) {
        int currentLocation = player.getCurrentLocation();
        int nextLocation = (currentLocation + move) % 32;
        //한 바퀴 돌았을 때
        if(currentLocation<nextLocation){
            int countLap = player.getCurrentLap()+1;
            long money = Math.round(player.getMoney() * 1.15);
            player.setMoney(money);
            player.setCurrentLap(countLap);
            // TODO: 배당금 관련 로직


            // TODO: 대출금 관련 로직

        }
        player.setCurrentLocation(nextLocation);
        return  player;



    }



    /**
     * 턴을 종료하는 로직
     * 1. 턴을 종료 후  다음 플레이어 확인(죽은 것도 생각)
     * 2. 마지막 플레이어면 턴 count 올리고
    * */

}
