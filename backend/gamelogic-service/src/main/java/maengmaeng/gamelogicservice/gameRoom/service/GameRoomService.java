package maengmaeng.gamelogicservice.gameRoom.service;

import lombok.AllArgsConstructor;
import maengmaeng.gamelogicservice.gameRoom.domain.*;
import maengmaeng.gamelogicservice.gameRoom.domain.db.*;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.Dice;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.GameStart;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.PlayerSeq;
import maengmaeng.gamelogicservice.gameRoom.repository.*;
import maengmaeng.gamelogicservice.global.dto.ResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GameRoomService {

    private final DbCountryRespository dbCountryRespository;
    private final DbNewsRepository dbNewsRepository;
    private final DbStockRepository dbStockRepository;
//    private final DbNewsCountryRepository dbNewsCountryRepository;
//    private final DbCardRepository dbCardRepository;
//    private final DbNewsStockRepository dbNewsStockRepository;
    private final GameInfoRepository gameInfoRepository;
    private final GameInfoMapper gameInfoMapper;
    private final AvatarRepository avatarRepository;
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

        StartCard[] cards = new StartCard[playerCnt];
        for(int i=0;i<playerCnt;i++){
            cards[i] = StartCard.builder().seq(i+1).selected(false).build();
        }
        Random random = new Random();
        for (int i = cards.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1); // 0부터 i까지 무작위 인덱스 선택
            // i와 j 위치의 요소 교환
            StartCard temp = cards[i];
            cards[i] = cards[j];
            cards[j] = temp;
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
                .seqCards(cards)
                .build();

        gameInfoRepository.createGameRoom(gameInfo);


        return GameStart.builder().cards(cards).build();

    }

    /**
     * 플레이어 게임 순서 세팅*/
    @Transactional
    public StartCard[] setPlayer(String roomCode, PlayerSeq playerSeq){

        GameInfo gameInfo = getInfo(roomCode);
        //
        StartCard[] startCards = gameInfo.getSeqCards();
        //
        Player[] players = gameInfo.getPlayers();
        int playerNum = gameInfo.getInfo().getPlayerCnt();
        Avatar avatar = avatarRepository.getReferenceById(playerSeq.getCharacterId());


        Player player = gameInfoMapper.toReidsPlayer(playerSeq.getUserId(), playerSeq.getNickname(),playerSeq.getCharacterId(),avatar.getAvatarImageNoBg());
        if(players[playerSeq.getPlayerCnt()-1] ==null && !startCards[playerSeq.getPlayerCnt()-1].isSelected()){
            players[playerSeq.getPlayerCnt()-1] = player;
            startCards[playerSeq.getPlayerCnt()-1].setSelected(true);

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
            gameInfo.setSeqCards(startCards);
        }

        gameInfoRepository.createGameRoom(gameInfo);
        return  gameInfo.getSeqCards();
    }

    /**
     * 현재 플레이어 인덱스 가져오기
     *
     * */
    public int getPlayerIdx(Player[] players, String currentPlayer){
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

        return Dice.builder().dice1(dice1).dice2(dice2).build();
    }

    /**
     * 주사위 굴리기.
    * */
    public ResponseDto rollDice(String roomCode){
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
            }
            curPlayer.setDoubleCount(doubleCount);

        }
        ResponseDto responseDto = null;
        int curLocation = curPlayer.getCurrentLocation();

        if(checkTrade){
            curPlayer.setCurrentLocation(stopTrade);
            // TODO: 거래정지

            players[currentIdx] = curPlayer;
            gameInfo.setPlayers(players);
            gameInfoRepository.createGameRoom(gameInfo);
            dice.setDoubleCount(curPlayer.getDoubleCount());
            // 거래 정지 칸으로 이동
            // 클라이언트에서 서버로 턴종료  호출
            responseDto = ResponseDto.builder().type("거래정지칸도착").data(dice).build();

            //TODO: 플레이어 정보도 다시 줘야 할까??


        } else{
            //
            Player player = move(curPlayer, dice.getDice1()+dice.getDice2());

            // 한바퀴 돌았으면
            if(curLocation >player.getCurrentLocation()){
                dice.setLapCheck(true);
                int currentLap = player.getCurrentLap()+1;

                player.setCurrentLap(currentLap);
            }
            players[currentIdx] = player;
            gameInfo.setPlayers(players);

            gameInfoRepository.createGameRoom(gameInfo);
            dice.setDoubleCount(curPlayer.getDoubleCount());

            responseDto = ResponseDto.builder().type("주사위").data(dice).build();
            //TODO: 플레이어 정보도 다시 줘야 할까??

        }


        return responseDto;
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
    public ResponseDto maengMaeng(/*(Player player, List<Stock> stocks, List<Land> lands,*/ GameInfo gameInfo){
        // 맹맹: 보유 현금 * 0.15 + 배당금 - 대출 원금 * 0.24)

        Player[] players = gameInfo.getPlayers();
        int playerIdx = getPlayerIdx(players,gameInfo.getInfo().getCurrentPlayer());
        Player player = players[playerIdx];
        List<Land> lands = gameInfo.getLands();
        List<Stock> stocks = gameInfo.getStocks();
        long maengMaeng =0;
        long money = Math.round(player.getMoney() * 0.15);
        long dividends = 0;
        long loan = Math.round(player.getLoan() *0.24);
        int[] playerStock = player.getStocks();
        // 배당금 구하기
        for(int i=1;i<playerStock.length;i++){
            dividends += playerStock[i] * stocks.get(i-1).getDividends();
        }
        maengMaeng = money+dividends -loan;
        // 맹맹 >=0 이면 보유 현금 +
        if(maengMaeng >=0){
            long playerMoney = player.getMoney();
            player.setMoney(playerMoney+maengMaeng);
            players[playerIdx] = player;
            gameInfo.setPlayers(players);
            gameInfoRepository.createGameRoom(gameInfo);
            return ResponseDto.builder().type("맹맹지급").data(player).build();

        } else{
            // 맹맹이 음수일 때
            // 맹맹이 보유자산 보다 많을 때?
            if(maengMaeng > calculateMoney(player,stocks,lands) ){
                //TODO: 파산 절차
                return  ResponseDto.builder().type("파산").build();

            } else {
                if (player.getMoney() -maengMaeng>=0 ){
                    // 보유 현금 -
                    player.setMoney(player.getMoney()-maengMaeng);
                    players[playerIdx] = player;
                    gameInfo.setPlayers(players);
                    gameInfoRepository.createGameRoom(gameInfo);
                    return ResponseDto.builder().type("맹맹지급").data(player).build();

                } else{
                    //TODO: 매각 절차
                    return ResponseDto.builder().type("매각시작").build();
                }
            }

        }

    }

    /**
     *  플레이어 화면에 보여줄 총자산 계산
     * */
    public Long calculateAsset(Player player,List<Stock> stocks, List<Land> lands){
        //TODO: asset 계산
        long asset =0;
        long stockMoney = 0;
        // 소유중인 주식 가격 구하기
        int[] playerStock = player.getStocks();
        for(int i=1;i<playerStock.length;i++){
            // 주식의 현재 가격 저장
            stockMoney += playerStock[i] * stocks.get(i-1).getCurrentCost();

        }
        asset += stockMoney;
        long landMoney =0;
        // 소유 중인 땅 가격 구하기
        List<Integer> landIdx = player.getLands();
        for(int idx: landIdx) {
            // 소유중인 나라 가져와서
            Land land = lands.get(idx);
            // 땅, 건물을 어떤것을 가지고 있는지 확인 후 가격 계산
            boolean[] check = land.getBuildings();
            for (int i = 0; i < check.length; i++) {
                // 땅 가지고 있을 때
                if (i == 0 && check[i]) {
                    landMoney += land.getCurrentLandPrice();
                }
                if (i == 1 && check[i]) {
                    landMoney += land.getCurrentBuildingPrices()[0];
                }
                if (i == 2 && check[i]) {
                    landMoney += land.getCurrentBuildingPrices()[1];
                }
                if (i == 3 && check[i]) {
                    landMoney +=  land.getCurrentBuildingPrices()[2];
                }
            }

        }

        asset += landMoney;

        return  asset;


    }
    /**
     * 파산 등에 사용할 자산 계산
     */

    public long calculateMoney(Player player, List<Stock> stocks, List<Land> lands){
        long asset =0;
        long stockMoney = 0;
        // 소유중인 주식 가격 구하기
        int[] playerStock = player.getStocks();
        for(int i=1;i<playerStock.length;i++){
            // 주식의 현재 가격 저장
            stockMoney += playerStock[i] * stocks.get(i-1).getCurrentCost();

        }
        asset += stockMoney;
        long landMoney =0;
        // 소유 중인 땅 가격 구하기
        List<Integer> landIdx = player.getLands();
        for(int idx: landIdx) {
            // 소유중인 나라 가져와서
            Land land = lands.get(idx);
            // 땅, 건물을 어떤것을 가지고 있는지 확인 후 가격 계산
            boolean[] check = land.getBuildings();
            for (int i = 0; i < check.length; i++) {
                // 땅 가지고 있을 때
                if (i == 0 && check[i]) {
                    landMoney +=  land.getCurrentLandPrice();
                }
                if (i == 1 && check[i]) {
                    landMoney +=  land.getCurrentBuildingPrices()[0];
                }
                if (i == 2 && check[i]) {
                    landMoney +=  land.getCurrentBuildingPrices()[1];
                }
                if (i == 3 && check[i]) {
                    landMoney +=  land.getCurrentBuildingPrices()[2];
                }
            }

        }

        asset += landMoney *0.7;

        return  asset;
    }



    /**
     * 이동 로직
     * */
    public Player move(Player player, int move) {
        int currentLocation = player.getCurrentLocation();
        int nextLocation = (currentLocation + move) % 32;
        player.setCurrentLocation(nextLocation);
        System.out.println(player.getCurrentLocation());
        return  player;

    }

    /**
     * 이동 후 로직
     * */
    public ResponseDto afterMove(String roomCode){
        GameInfo gameInfo = getInfo(roomCode);
        Player[] players = gameInfo.getPlayers();
        int playerIdx = getPlayerIdx(players,gameInfo.getInfo().getCurrentPlayer());
        // 현재 플레이어
        Player curPlayer = players[playerIdx];
        int currentLocation = curPlayer.getCurrentLocation();
        /* 이동 후에 위치에 따라서 로직...*/
        /**/

        ResponseDto responseDto = null;

        switch(currentLocation){
            case 0:
                // 시작지점
                responseDto = ResponseDto.builder().type("시작지점").build();
                break;
            case 2:
                // 세금 징수

                responseDto = ResponseDto.builder().type("세금징수").build();

                break;
            case 4:
            case 12:
            case 20:
            case 28:
                // 황금 열쇠
                responseDto= ResponseDto.builder().type("황금열쇠").build();
                break;
            case 8:
                // 거래정지
                responseDto = ResponseDto.builder().type("거래정지").build();
                break;
            case 10:
                // 박진호
                responseDto = ResponseDto.builder().type("박진호").build();
                break;
            case 16:
                // 투자장
                responseDto = ResponseDto.builder().type("거래장").build();
                break;

            case 18:
                // Rush & Cash
                responseDto = ResponseDto.builder().type("대출").build();
                break;
            case 24:
                // 어디로든 문
                //
                if(gameInfo.getInfo().getDoorCheck()>0){
                 responseDto = ResponseDto.builder().type("강준구의문단속적용어디로든문").build();
                } else{
                    responseDto = ResponseDto.builder().type("어디로든문").build();
                }
                break;
            default :
                // 일반 땅
                // 땅이 내 땅인지 중립땅인지 다른 플레이어의 땅인지 판별
                List<Land> lands = gameInfo.getLands();
                if(lands.get(currentLocation).getOwner()==-1){
                    // 중립 땅일 때
                    responseDto = ResponseDto.builder().type("땅구매").build();
                } else if(lands.get(currentLocation).getOwner()==playerIdx){
                    // 내땅 일 때
                    responseDto = ResponseDto.builder().type("건물구매").build();
                } else {
                    // 다른 플레이어의 소유 일 때
                    // 통행료 계산
                    long fees =  lands.get(currentLocation).getCurrentFees()[0];
                    for (int i = 0; i < lands.get(currentLocation).getCurrentFees().length; i++) {
                        if (lands.get(currentLocation).getBuildings()[i]) {
                            fees += lands.get(currentLocation).getCurrentFees()[i + 1];
                        }
                    }
                    long asset = calculateAsset(players[playerIdx],gameInfo.getStocks(),lands);
                    if( fees<=players[playerIdx].getMoney()){
                        // 현금으로 통행료 지급이 가능할 때
                        responseDto = ResponseDto.builder().type("통행료지급").build();
                    } else{
                        // 현금으로 통행료 지급이 불가능 할 때
                        if(asset>= fees){
                            // 매각할 수 있으면
                            responseDto = ResponseDto.builder().type("매각").build();
                        } else{
                            // 파산각이면
                            responseDto = ResponseDto.builder().type("파산").build();
                        }

                    }
                }
        }

        return responseDto;
    }



    /**
     * 턴을 종료하는 로직
     * 1. 현재 플레이어의 turnCount올리기
     * 2. 다음 플레이어 확인 for 문 순회
     * 3. waiting news 확인해서 effect news 적용
     * 4.
    * */
    public ResponseDto endTurn(String roomCode){
        GameInfo gameInfo = gameInfoRepository.getGameInfo(roomCode);
        Player[] players = gameInfo.getPlayers();
        int playerIdx = getPlayerIdx(players,gameInfo.getInfo().getCurrentPlayer());
        Player currentPlayer = players[playerIdx];
        // 현재 플레이어의 turnCount++
        int currentTurn = currentPlayer.getCurrentTurn() +1;
        currentPlayer.setCurrentTurn(currentTurn);

        Info info = gameInfo.getInfo();
//        int doorCheck = info.getDoorCheck();
        // 턴을 넘기기 전에 현재 플레이어가 마지막 플레이어인지 확인.
        // 2인 3인 이면
        boolean isLastPlayer = false;
        // 배열의 마지막 사람이면 무조건 제일 마지막 플레이어
        if(info.getPlayerCnt() == playerIdx+1){
            isLastPlayer = true;
        } else{
            // 마지막 까지 배열을 순회하면서 살아있는 사람이 없으면 마지막 플레이어.
            for(int i= playerIdx ; i< info.getPlayerCnt();i++){
                if(players[i].isAlive()){
                    isLastPlayer = false;
                    break;
                }
            }

        }



        if(isLastPlayer){
            //TODO: 턴이 바뀔 때 수행되어야하는 로직
            //TODO: effectNews, waitingNews 관련 로직
        }


        // 다음 플레이어
        int nextPlayerIdx = -1;
        // 다음 플레이어 idx 구하는 로직
        for(int i=1;i<4;i++){
            int idx = (playerIdx + i) % 4;
            if(players[idx] !=null && players[idx].isAlive()){
                nextPlayerIdx = idx;
                break;
            }

        }
        if(nextPlayerIdx!=-1){
            // 다음 플레이어 순서를 설정
            info.setCurrentPlayer(players[nextPlayerIdx].getNickname());
        }









        return null;
    }



}
