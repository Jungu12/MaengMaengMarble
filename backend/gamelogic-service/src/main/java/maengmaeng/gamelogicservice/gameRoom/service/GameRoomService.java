package maengmaeng.gamelogicservice.gameRoom.service;

import lombok.AllArgsConstructor;
import maengmaeng.gamelogicservice.gameRoom.domain.*;
import maengmaeng.gamelogicservice.gameRoom.domain.db.*;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.*;
import maengmaeng.gamelogicservice.gameRoom.repository.*;
import maengmaeng.gamelogicservice.global.dto.ResponseDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private final DbNewsCountryRepository dbNewsCountryRepository;
	private final DbCardRepository dbCardRepository;
	private final DbNewsStockRepository dbNewsStockRepository;
	private final GameInfoRepository gameInfoRepository;
	private final GameInfoMapper gameInfoMapper;
	private final AvatarRepository avatarRepository;
	private static final int stopTrade = 8;
	private final Logger logger = LoggerFactory.getLogger(getClass());


	/**
	 * 게임 정보  가져오기
	 * Params: roomCode
	 * return GameInfo
	 * */
	public GameInfo getInfo(String roomCode) {
		return gameInfoRepository.getGameInfo(roomCode);

	}

	/**
	 * 처음 시작 카드 선택
	 * */
	public synchronized GameStart setStart(String roomCode, int playerCnt) {

		StartCard[] cards = new StartCard[playerCnt];
		for (int i = 0; i < playerCnt; i++) {
			cards[i] = StartCard.builder().seq(i + 1).selected(false).build();
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

		List<News> platinumNews = dbNewsRepository.findByNewsType("Platinum")
			.stream()
			.map(gameInfoMapper::toRedisNews)
			.collect(Collectors.toList());
		Collections.shuffle(platinumNews);
		List<News> diamondNews = dbNewsRepository.findByNewsType("Diamond")
			.stream()
			.map(gameInfoMapper::toRedisNews)
			.collect(Collectors.toList());
		Collections.shuffle(diamondNews);
		List<News> bronzeNews = dbNewsRepository.findByNewsType("Bronze")
			.stream()
			.map(gameInfoMapper::toRedisNews)
			.collect(Collectors.toList());
		Collections.shuffle(bronzeNews);
		int bronze = bronzeNews.size();
		int diamond = diamondNews.size();
		int platinum = platinumNews.size();
		List<Land> landList = dbCountryList.stream().map(gameInfoMapper::toRedisLand).collect(Collectors.toList());
		List<Stock> stockList = dbStockRepository.findAll()
			.stream()
			.map(gameInfoMapper::toRedisStock)
			.collect(Collectors.toList());

		GameInfo gameInfo = GameInfo.builder()
			.roomCode(roomCode)
			.players(new Player[4])
			.lands(landList)
			.info(Info.builder().playerCnt(playerCnt).build())
			.goldenKeys(gameInfoMapper.toRedisGoldenKeys(bronze, diamond, platinum))
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
	public StartCard[] setPlayer(String roomCode, PlayerSeq playerSeq) {

		GameInfo gameInfo = getInfo(roomCode);
		StartCard[] startCards = gameInfo.getSeqCards();
		Player[] players = gameInfo.getPlayers();
		int playerNum = gameInfo.getInfo().getPlayerCnt();
		Avatar avatar = avatarRepository.getReferenceById(playerSeq.getCharacterId());
		Player player = gameInfoMapper.toReidsPlayer(playerSeq.getUserId(), playerSeq.getNickname(),
			playerSeq.getCharacterId(), avatar.getAvatarImageNoBg());
		int seq = startCards[playerSeq.getPlayerCnt()].getSeq();
		if (players[seq - 1] == null && !startCards[playerSeq.getPlayerCnt()].isSelected()) {
			players[seq - 1] = player;
			startCards[playerSeq.getPlayerCnt()].setSelected(true);

			if (seq == 1) {
				Info info = Info.builder()
					.currentPlayer(players[seq-1].getNickname())
					.playerCnt(playerNum)
					.turnCount(1)
					.effectNews(new LinkedList<>())
					.waitingNews(new PriorityQueue<WaitingNews>(new WaitingNewsComparator()))
					.build();
				gameInfo.setInfo(info);
			}
			gameInfo.setSeqCards(startCards);
		}

		gameInfoRepository.createGameRoom(gameInfo);



		return gameInfo.getSeqCards();
	}

	/**
	 * 현재 플레이어 인덱스 가져오기
	 *
	 * */
	public int getPlayerIdx(Player[] players, String currentPlayer) {
		int currentIdx = -1;
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null && players[i].isAlive() && players[i].getNickname().equals(currentPlayer)) {
				currentIdx = i;
			}
		}
		return currentIdx;
	}

	/**
	 * 주사위 눈 반환
	 * */
	public Dice getDice() {
		Random random = new Random();

		 // // 주사위 1 던지기 (1부터 6까지)
		 // int dice1 = random.nextInt(6) + 1;
		 //
		 // // 주사위 2 던지기 (1부터 6까지)
		 // int dice2 = random.nextInt(6) + 1;

		int dice1 = 1;
		int dice2 = 2;

		return Dice.builder().dice1(dice1).dice2(dice2).build();
	}

	/**
	 * 주사위 굴리기.
	 * */
	public ResponseDto rollDice(String roomCode) {
		// 게임 정보 가져오기
		GameInfo gameInfo = gameInfoRepository.getGameInfo(roomCode);
		Player[] players = gameInfo.getPlayers();
		String currentPlayer = gameInfo.getInfo().getCurrentPlayer();

		int currentIdx = -1;
		// 현재 플레이어 인덱스 찾기
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null && players[i].isAlive() && players[i].getNickname().equals(currentPlayer)) {
				currentIdx = i;
				break;
			}
		}
		logger.info("rollDice(), roomCode ={},currentPlayer = {} ", roomCode, players[currentIdx].getNickname());

		if (currentIdx != -1) {
			// 예외 처리

		}
		// 주사위 굴리기
		Dice dice = getDice();
		Player curPlayer = players[currentIdx];

		boolean checkTrade = false;
		// 더블 일 때
		if (dice.getDice1() == dice.getDice2()) {
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

		if (checkTrade) {
			logger.info("doubleCount = {}",curPlayer.getDoubleCount());
			curPlayer.setCurrentLocation(stopTrade);
			players[currentIdx] = curPlayer;
			gameInfo.setPlayers(players);
			gameInfoRepository.createGameRoom(gameInfo);
			dice.setDoubleCount(curPlayer.getDoubleCount());
			dice.setPlayers(players);
			// 거래 정지 칸으로 이동
			// 클라이언트에서 서버로 턴종료  호출
			logger.info("주사위턴종료호출");
			responseDto = ResponseDto.builder().type("주사위턴종료").data(dice).build();

		} else {
			//
			Player player = move(curPlayer, dice.getDice1() + dice.getDice2());

			// 한바퀴 돌았으면
			if (curLocation > player.getCurrentLocation()) {
				dice.setLapCheck(true);
				int currentLap = player.getCurrentLap() + 1;

				player.setCurrentLap(currentLap);
				players[currentIdx] = player;
				gameInfo.setPlayers(players);

				gameInfoRepository.createGameRoom(gameInfo);
				dice.setDoubleCount(curPlayer.getDoubleCount());
				dice.setPlayers(players);
				logger.info("주사위 맹맹지급");
				responseDto = ResponseDto.builder().type("주사위맹맹지급").data(dice).build();

			} else{
				players[currentIdx] = player;
				gameInfo.setPlayers(players);

				gameInfoRepository.createGameRoom(gameInfo);
				dice.setDoubleCount(curPlayer.getDoubleCount());
				dice.setPlayers(players);
				logger.info("주사위이동후로직");
				responseDto = ResponseDto.builder().type("주사위이동후로직").data(dice).build();

			}



		}

		return responseDto;
	}

	/**
	 *  현재 칸이 거래 정지에 있고 player의 doubleCount가 3이 아닐 때 주사위 굴리기
	 * */
	public ResponseDto stopTrade(String roomCode) {
		// 더블이면 탈출 or stopTradeCount 값이 3이면 탈출
		GameInfo gameInfo = getInfo(roomCode);
		Player[] players = gameInfo.getPlayers();

		String currentPlayer = gameInfo.getInfo().getCurrentPlayer();

		int currentIdx = -1;
		// 현재 플레이어 인덱스 찾기
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null && players[i].isAlive() && players[i].getNickname().equals(currentPlayer)) {
				currentIdx = i;
			}
		}
		Player curPlayer = players[currentIdx];
		logger.info("stopTrade(),roomCode = {}, currentPlayer = {} ",roomCode,players[currentIdx].getNickname());
		// 현재 위치가 stopTrade 위치가 아니면
		if (curPlayer.getCurrentLocation() != stopTrade) {
			// 예외 처리
			System.out.println("예외");
		}

		// 주사위 던지기
		Dice dice = getDice();
		int stopTradeCount = curPlayer.getStopTradeCount();
		// 더블이 아닐 때
		if (dice.getDice1() != dice.getDice2()) {
			// 플레이어의 stopTradeCount 증가
			stopTradeCount++;
			curPlayer.setStopTradeCount(stopTradeCount);
			players[currentIdx] = curPlayer;
			dice.setPlayers(players);
			gameInfo.setPlayers(players);
			gameInfoRepository.createGameRoom(gameInfo);
			logger.info("탈출 실패");
			return ResponseDto.builder().type("거래정지턴종료").data(dice).build();
		} else {
			// 더블이면 이동
			int curLocation = curPlayer.getCurrentLocation();
			Player player = move(curPlayer, dice.getDice1() + dice.getDice2());
			// 플레이어의 stopTradeCount값 0으로 초기화
			player.setStopTradeCount(0);
			// 한바퀴 돌았으면
			if (curLocation > player.getCurrentLocation()) {
				dice.setLapCheck(true);
				int currentLap = player.getCurrentLap() + 1;
				player.setCurrentLap(currentLap);
			}
			players[currentIdx] = player;
			gameInfo.setPlayers(players);
			gameInfoRepository.createGameRoom(gameInfo);
			// 거래정지탈출 이후엔 더블이어도 한 번더 던질 수 없음
			dice.setDoubleCount(0);
			dice.setPlayers(players);
			logger.info("더블나와서 탈출");
			return ResponseDto.builder().type("거래정지이동후로직").data(dice).build();

		}

	}

	/**
	 * 토지 및 건물 구매
	 * @param roomCode
	 * @param purchasedBuildings : 길이 4의 boolean 배열, 순서대로 토지, 건물1, 건물2, 건물 3의 구매 여부를 담은 배열
	 */
	public ResponseDto purchaseAndUpdateGameInfo(String roomCode, boolean[] purchasedBuildings) {
		// 게임 정보 가져오기
		GameInfo gameInfo = gameInfoRepository.getGameInfo(roomCode);
		Player[] players = gameInfo.getPlayers();
		List<Land> lands = gameInfo.getLands();

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

		//현재 플레이어의 위치 가져오기
		Player curPlayer = players[currentIdx];
		Land curLand = gameInfo.getLands().get(curPlayer.getCurrentLocation());

		// 지불 금액에 땅 값 추가
		int totalPay = curLand.getLandPrice();

		// 지불 금액에 구매한 건물 추가
		int[] buildingPrices = curLand.getBuildingPrices();
		for (int idx = 0; idx < purchasedBuildings.length; idx++) {
			if (purchasedBuildings[idx]) {
				totalPay += buildingPrices[idx];

			}
		}

		List<Integer> curPlayerLands = curPlayer.getLands();
		curPlayerLands.add(curLand.getLandId());

		curPlayer.setLands(curPlayerLands);
		curPlayer.setMoney(curPlayer.getMoney() - totalPay);
		curLand.setOwner(currentIdx);
		curLand.setBuildings(purchasedBuildings);

		players[currentIdx] = curPlayer;
		gameInfo.setPlayers(players);

		lands.set(curLand.getLandId(), curLand);
		gameInfo.setLands(lands);

		gameInfoRepository.createGameRoom(gameInfo);

		return ResponseDto.builder()
			.type("자유")
			.data(gameInfo)
			.build();
	}

	/**
	 * 매각. 매각은 건물 단위가 아닌 토지 단위로 이루어진다.
	 * @param roomCode
	 * @param landId 매각하는 토지 ID
	 */
	public ResponseDto forSale(String roomCode, int landId) {
		// 게임 정보 가져오기
		GameInfo gameInfo = gameInfoRepository.getGameInfo(roomCode);
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

		int currentLandPrice = 0;
		Land saledLand = gameInfo.getLands().get(landId);

		for (int i = 0; i < saledLand.getBuildingPrices().length; i++) {
			if (saledLand.getBuildings()[i]) {
				currentLandPrice += saledLand.getCurrentBuildingPrices()[i];
			}
		}

		saledLand.setOwner(-1);
		saledLand.setBuildings(new boolean[] {false, false, false});

		//Player 객체 안의 보유 땅 정보 업데이트
		List<Integer> curPlayerLands = players[currentIdx].getLands();
		curPlayerLands.remove(saledLand.getLandId());
		players[currentIdx].setLands(curPlayerLands);

		players[currentIdx].setMoney(players[currentIdx].getMoney() + (long)(currentLandPrice * 0.7));

		//gameInfo에 바뀐 정보 최신화
		gameInfo.getLands().set(landId, saledLand);
		gameInfo.setPlayers(players);

		//Redis에 바뀐 정보 업데이트
		gameInfoRepository.createGameRoom(gameInfo);

		return ResponseDto.builder()
			.type("자유")
			.data(gameInfo)
			.build();
	}

	/**
	 * 통행료 지불
	 * @param roomCode
	 */
	public ResponseDto payFee(String roomCode) {
		// 게임 정보 가져오기
		GameInfo gameInfo = gameInfoRepository.getGameInfo(roomCode);
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

		//플레이어의 현재 위치
		int landId = players[currentIdx].getCurrentLocation();

		Land currentLand = gameInfo.getLands().get(landId);
		int currentLandFee = currentLand.getCurrentFees()[0];

		for (int i = 0; i < currentLand.getBuildings().length; i++) {
			if (currentLand.getBuildings()[i]) {
				currentLandFee += currentLand.getCurrentFees()[i + 1];
			}
		}

		// 만큼 현재 플레이어의 보유 자산 및 보유 현금 차감
		players[currentIdx].setMoney(players[currentIdx].getMoney() - currentLandFee);
		players[currentIdx].setAsset(players[currentIdx].getAsset() - currentLandFee);

		// 통행료 만큼 땅 주인의 보유 자산 및 보유 현금 증감
		int owner = currentLand.getOwner();
		players[owner].setMoney(players[owner].getMoney() + currentLandFee);
		players[owner].setAsset(players[owner].getAsset() + currentLandFee);

		// 바뀐 정보 gameInfo에 업데이트
		gameInfo.setPlayers(players);

		gameInfoRepository.createGameRoom(gameInfo);

		return ResponseDto.builder().type("인수").data(players).build();
	}

	/**료
	 * 인수
	 * @param roomCode
	 */
	public ResponseDto takeOver(String roomCode) {
		// 게임 정보 가져오기
		GameInfo gameInfo = gameInfoRepository.getGameInfo(roomCode);
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

		//플레이어의 현재 위치
		int landId = players[currentIdx].getCurrentLocation();

		Land currentLand = gameInfo.getLands().get(landId);
		int currentLandFee = currentLand.getCurrentFees()[0];

		for (int i = 0; i < currentLand.getBuildings().length; i++) {
			if (currentLand.getBuildings()[i]) {
				currentLandFee += currentLand.getCurrentFees()[i + 1];
			}
		}

		// 통행료 만큼 현재 플레이어의 보유 자산 및 보유 현금 차감
		players[currentIdx].setMoney(players[currentIdx].getMoney() - currentLandFee);
		players[currentIdx].setAsset(players[currentIdx].getAsset() - currentLandFee);

		// 통행료 만큼 땅 주인의 보유 자산 및 보유 현금 증감
		int owner = currentLand.getOwner();
		players[owner].setMoney(players[owner].getMoney() + currentLandFee);
		players[owner].setAsset(players[owner].getAsset() + currentLandFee);

		// 인수 로직 처리 후 땅 주인 변경
		currentLand.setOwner(currentIdx);

		//Player 객체 안의 보유 땅 정보 업데이트
		List<Integer> curPlayerLands = players[currentIdx].getLands();
		curPlayerLands.remove(currentLand.getLandId());
		players[currentIdx].setLands(curPlayerLands);

		//gameInfo에 바뀐 정보 최신화
		gameInfo.setPlayers(players);
		gameInfo.getLands().set(currentLand.getLandId(), currentLand);

		//Redis에 gameInfo 업데이트
		gameInfoRepository.createGameRoom(gameInfo);

		// 바뀐 정보 return
		return ResponseDto.builder()
			.type("자유")
			.data(gameInfo)
			.build();
	}

	public ResponseDto chooseGoldenKeys(String roomCode) {
		// 게임 정보 가져오기
		GameInfo gameInfo = gameInfoRepository.getGameInfo(roomCode);
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

		Player curPlayer = players[currentIdx];

		Random random = new Random();
		GoldenKeys goldenKeys = gameInfo.getGoldenKeys();
		int totalProbability = goldenKeys.getBronze() + goldenKeys.getDiamond() + goldenKeys.getPlatinum() +
			goldenKeys.getNewsBan() + goldenKeys.getHurricane() + goldenKeys.getAngel() +
			goldenKeys.getKangJunGu() + goldenKeys.getLotto() + goldenKeys.getDoor() + goldenKeys.getEarthquake();

		int randomValue = random.nextInt(totalProbability);
		//황금 열쇠 종류
		String cardType;
		ResponseDto responseDto = ResponseDto.builder().build();

		if (randomValue < goldenKeys.getBronze()) {
			cardType = "브론즈";
			goldenKeys.setBronze(goldenKeys.getBronze() - 1);

			List<News> bronzes = gameInfo.getNewsInfo().getBronze();
			//반환할 뉴스 3개
			List<News> choosed = new ArrayList<>(bronzes.subList(0, 3));

			//선택 된 뉴스 삭제
			Iterator<News> iterator = bronzes.iterator();
			for (int i = 0; i < 3 && iterator.hasNext(); i++) {
				iterator.next();
				iterator.remove();
			}

			//gameInfo에 바뀐 뉴스 정보 업데이트
			gameInfo.getNewsInfo().setBronze(bronzes);

			responseDto = ResponseDto.builder()
				.type(cardType)
				.data(NewsResponse.builder().choosed(choosed).goldenKeys(goldenKeys).build())
				.build();

		} else if (randomValue < goldenKeys.getBronze() + goldenKeys.getPlatinum()) {
			cardType = "플레티넘";
			goldenKeys.setPlatinum(goldenKeys.getPlatinum() - 1);

			List<News> platinums = gameInfo.getNewsInfo().getPlatinum();
			//반환할 뉴스 3개
			List<News> choosed = new ArrayList<>(platinums.subList(0, 3));


			//선택 된 뉴스 삭제
			Iterator<News> iterator = platinums.iterator();
			for (int i = 0; i < 3 && iterator.hasNext(); i++) {
				iterator.next();
				iterator.remove();
			}

			//gameInfo에 바뀐 뉴스 정보 업데이트
			gameInfo.getNewsInfo().setPlatinum(platinums);

			responseDto = ResponseDto.builder()
				.type(cardType)
				.data(NewsResponse.builder().choosed(choosed).goldenKeys(goldenKeys).build())
				.build();

		} else if (randomValue < goldenKeys.getBronze() + goldenKeys.getPlatinum() + goldenKeys.getDiamond()) {
			cardType = "다이아몬드";
			goldenKeys.setDiamond(goldenKeys.getDiamond() - 1);

			List<News> diamonds = gameInfo.getNewsInfo().getDiamond();
			//반환할 뉴스 3개
			List<News> choosed = new ArrayList<>(diamonds.subList(0, 3));


			//선택 된 뉴스 삭제
			Iterator<News> iterator = diamonds.iterator();
			for (int i = 0; i < 3 && iterator.hasNext(); i++) {
				iterator.next();
				iterator.remove();
			}

			//gameInfo에 바뀐 뉴스 정보 업데이트
			gameInfo.getNewsInfo().setDiamond(diamonds);

			responseDto = ResponseDto.builder()
				.type(cardType)
				.data(NewsResponse.builder().choosed(choosed).goldenKeys(goldenKeys).build())
				.build();

		} else if (randomValue
			< goldenKeys.getBronze() + goldenKeys.getPlatinum() + goldenKeys.getDiamond() + goldenKeys.getNewsBan()) {
			cardType = "언론통제";
			goldenKeys.setNewsBan(goldenKeys.getNewsBan() - 1);

			//현재 플레이어의 카드 정보
			boolean[] curPlayerCards = curPlayer.getCards();

			//언론 통제 카드 보유 중으로 변경
			curPlayerCards[1] = true;
			curPlayer.setCards(curPlayerCards);

			//gameInfo에 바뀐 정보 업데이트
			players[currentIdx] = curPlayer;
			gameInfo.setPlayers(players);

			responseDto = ResponseDto.builder()
				.type(cardType)
				.data(GoldenKeysPlayersResponse.builder().players(players).goldenKeys(goldenKeys).imgUrl(
					goldenKeys.getNewsBanImgUrl()).build())
				.build();

		} else if (randomValue
			< goldenKeys.getBronze() + goldenKeys.getPlatinum() + goldenKeys.getDiamond() + goldenKeys.getNewsBan()
			+ goldenKeys.getHurricane()) {
			cardType = "허리케인";
			goldenKeys.setHurricane(goldenKeys.getHurricane() - 1);

			List<Land> lands = gameInfo.getLands();

			for (int i = 0; i < lands.size(); i++) {
				Land curLand = lands.get(i);

				// 내 땅이거나, 중립 땅이면 continue
				if (curLand.getOwner() == (currentIdx) || curLand.getOwner() == -1) {
					continue;
				}

				//제일 비싼 건물 하나 날리기
				for (int j = 0; j < curLand.getBuildings().length; j++) {
					boolean[] buildingInfo = curLand.getBuildings();

					if (buildingInfo[buildingInfo.length - j - 1]) {
						buildingInfo[buildingInfo.length - j - 1] = false;
						curLand.setBuildings(buildingInfo);
						lands.set(j, curLand);

						break;
					}
				}
			}

			//gameInfo에 바뀐 땅 정보 업데이트
			gameInfo.setLands(lands);

			responseDto = ResponseDto.builder()
				.type(cardType)
				.data(GoldenKeysLandsResponse.builder().Lands(lands).goldenKeys(goldenKeys).imgUrl(
					goldenKeys.getHurricaneImgUrl()).build())
				.build();

		} else if (randomValue
			< goldenKeys.getBronze() + goldenKeys.getPlatinum() + goldenKeys.getDiamond() + goldenKeys.getNewsBan()
			+ goldenKeys.getHurricane() + goldenKeys.getAngel()) {
			cardType = "천사";
			goldenKeys.setAngel(goldenKeys.getAngel() - 1);

			//현재 플레이어의 카드 정보
			boolean[] curPlayerCards = curPlayer.getCards();

			//천사 카드 보유 중으로 변경
			curPlayerCards[0] = true;
			curPlayer.setCards(curPlayerCards);

			//gameInfo에 바뀐 정보 업데이트
			players[currentIdx] = curPlayer;
			gameInfo.setPlayers(players);

			responseDto = ResponseDto.builder()
				.type(cardType)
				.data(GoldenKeysPlayersResponse.builder().players(players).goldenKeys(goldenKeys).imgUrl(
					goldenKeys.getAngelImgUrl()).build())
				.build();

		} else if (randomValue
			< goldenKeys.getBronze() + goldenKeys.getPlatinum() + goldenKeys.getDiamond() + goldenKeys.getNewsBan()
			+ goldenKeys.getHurricane() + goldenKeys.getAngel() + goldenKeys.getKangJunGu()) {
			cardType = "강준구의 문단속";
			goldenKeys.setKangJunGu(goldenKeys.getKangJunGu() - 1);
			gameInfo.getInfo().setDoorCheck(5);

			//gameInfo에 바뀐 정보 업데이트
			players[currentIdx] = curPlayer;
			gameInfo.setPlayers(players);

			responseDto = ResponseDto.builder()
				.type(cardType)
				.data(GoldenKeysInfoResponse.builder().info(gameInfo.getInfo()).goldenKeys(goldenKeys).imgUrl(goldenKeys.getKangJunGuImgUrl()).build())
				.build();
		} else if (randomValue
			< goldenKeys.getBronze() + goldenKeys.getPlatinum() + goldenKeys.getDiamond() + goldenKeys.getNewsBan()
			+ goldenKeys.getHurricane() + goldenKeys.getAngel() +
			goldenKeys.getKangJunGu() + goldenKeys.getLotto()) {
			cardType = "복권 당첨";
			goldenKeys.setLotto(goldenKeys.getLotto() - 1);

			//현재 플레이어의 보유 현금의 * 10, 보유 자산도 업데이트
			curPlayer.setAsset(curPlayer.getAsset() + curPlayer.getMoney() * 9);
			curPlayer.setMoney(curPlayer.getMoney() * 10);

			//gameInfo에 바뀐 정보 업데이트
			players[currentIdx] = curPlayer;
			gameInfo.setPlayers(players);

			responseDto = ResponseDto.builder()
				.type(cardType)
				.data(GoldenKeysPlayersResponse.builder().players(players).goldenKeys(goldenKeys).imgUrl(
					goldenKeys.getLottoImgUrl()).build())
				.build();
		} else if (randomValue
			< goldenKeys.getBronze() + goldenKeys.getPlatinum() + goldenKeys.getDiamond() + goldenKeys.getNewsBan()
			+ goldenKeys.getHurricane() + goldenKeys.getAngel() +
			goldenKeys.getKangJunGu() + goldenKeys.getLotto() + goldenKeys.getDoor()) {
			cardType = "어디로든 문 초대권";
			goldenKeys.setDoor(goldenKeys.getDoor() - 1);

			//어디로든 문 위치로 이동
			curPlayer.setCurrentLocation(24);

			//gameInfo에 바뀐 정보 업데이트
			players[currentIdx] = curPlayer;
			gameInfo.setPlayers(players);

			responseDto = ResponseDto.builder()
				.type(cardType)
				.data(GoldenKeysPlayersResponse.builder().players(players).goldenKeys(goldenKeys).imgUrl(goldenKeys.getDoorImgUrl()).build())
				.build();
		} else {
			cardType = "지진";
			goldenKeys.setEarthquake(goldenKeys.getEarthquake() - 1);

			List<Land> lands = gameInfo.getLands();

			for (int i = 0; i < lands.size(); i++) {
				Land curLand = lands.get(i);

				// 상대 땅이거나, 중립 땅이면 continue
				if (curLand.getOwner() != (currentIdx) || curLand.getOwner() == -1) {
					continue;
				}

				//제일 비싼 건물 하나 날리기
				for (int j = 0; j < curLand.getBuildings().length; j++) {
					boolean[] buildingInfo = curLand.getBuildings();

					if (buildingInfo[buildingInfo.length - j - 1]) {
						buildingInfo[buildingInfo.length - j - 1] = false;
						curLand.setBuildings(buildingInfo);
						lands.set(j, curLand);

						break;
					}
				}
			}

			//gameInfo에 바뀐 땅 정보 업데이트
			gameInfo.setLands(lands);

			responseDto = ResponseDto.builder()
				.type(cardType)
				.data(GoldenKeysLandsResponse.builder().Lands(lands).goldenKeys(goldenKeys).imgUrl(
					goldenKeys.getEarthquakeImgUrl()).build())
				.build();
		}

		//Redis에 바뀐 정보 업데이트
		gameInfo.setGoldenKeys(goldenKeys);
		gameInfoRepository.createGameRoom(gameInfo);
		logger.info("type={}", cardType);
		return responseDto;
	}

	/**
	 * 맹맹 지급
	 * */
	public ResponseDto maengMaeng(GameInfo gameInfo) {
		// 맹맹: 보유 현금 * 0.15 + 배당금 - 대출 원금 * 0.24)
		Player[] players = gameInfo.getPlayers();
		int playerIdx = getPlayerIdx(players, gameInfo.getInfo().getCurrentPlayer());
		logger.info("maengMaeng(), currentPlayer ={}",players[playerIdx].getNickname());
		Player player = players[playerIdx];
		List<Land> lands = gameInfo.getLands();
		List<Stock> stocks = gameInfo.getStocks();
		long maengMaeng = 0;
		long money = Math.round(player.getMoney() * 0.15);
		long dividends = 0;
		long loan = Math.round(player.getLoan() * 0.24);
		int[] playerStock = player.getStocks();
		// 배당금 구하기
		for (int i = 1; i < playerStock.length; i++) {
			dividends += playerStock[i] * stocks.get(i - 1).getDividends();
		}
		maengMaeng = money + dividends - loan;
		// 맹맹 >=0 이면 보유 현금 +
		if (maengMaeng >= 0) {
			long playerMoney = player.getMoney();
			player.setMoney(playerMoney + maengMaeng);
			//TODO: asset 값 올리기
			long playerAsset = player.getAsset();
			player.setAsset(playerAsset + maengMaeng);
			players[playerIdx] = player;
			gameInfo.setPlayers(players);
			gameInfoRepository.createGameRoom(gameInfo);
			logger.info("type= 맹맹지급이동후로직");
			return ResponseDto.builder().type("맹맹지급이동후로직").data(players).build();
		} else {
			// 맹맹이 음수일 때
			// 맹맹이 보유자산 보다 많을 때?
			if (maengMaeng > calculateMoney(player, stocks, lands)) {
				logger.info("type=파산");
				return ResponseDto.builder().type("파산").build();
			} else {
				if (player.getMoney() - maengMaeng >= 0) {
					// 보유 현금 -
					player.setMoney(player.getMoney() - maengMaeng);
					player.setAsset(player.getAsset() - maengMaeng);
					players[playerIdx] = player;
					gameInfo.setPlayers(players);
					gameInfoRepository.createGameRoom(gameInfo);
					logger.info("type=맹맹지급이동후로직");

					return ResponseDto.builder().type("맹맹지급이동후로직").data(players).build();
				} else {
					logger.info("type=매각");

					return ResponseDto.builder().type("매각").data(NeedSaleMoney.builder().needMoney(maengMaeng).build()).build();
				}
			}

		}

	}

	/**
	 *  플레이어 화면에 보여줄 총자산 계산
	 * */
	public Long calculateAsset(Player player, List<Stock> stocks, List<Land> lands) {
		//TODO: asset 계산
		long asset = 0;
		long stockMoney = 0;
		// 소유중인 주식 가격 구하기
		int[] playerStock = player.getStocks();
		for (int i = 1; i < playerStock.length; i++) {
			// 주식의 현재 가격 저장
			stockMoney += playerStock[i] * stocks.get(i - 1).getCurrentCost();

		}
		asset += stockMoney;
		long landMoney = 0;
		// 소유 중인 땅 가격 구하기
		List<Integer> landIdx = player.getLands();
		for (int idx : landIdx) {
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
					landMoney += land.getCurrentBuildingPrices()[2];
				}
			}

		}

		asset += landMoney;

		return asset;

	}

	/**
	 * 파산 등에 사용할 자산 계산
	 */

	public long calculateMoney(Player player, List<Stock> stocks, List<Land> lands) {
		long asset = 0;
		long stockMoney = 0;
		// 소유중인 주식 가격 구하기
		int[] playerStock = player.getStocks();
		for (int i = 1; i < playerStock.length; i++) {
			// 주식의 현재 가격 저장
			stockMoney += playerStock[i] * stocks.get(i - 1).getCurrentCost();
		}
		asset += stockMoney;
		long landMoney = 0;
		// 소유 중인 땅 가격 구하기
		List<Integer> landIdx = player.getLands();
		for (int idx : landIdx) {
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
					landMoney += land.getCurrentBuildingPrices()[2];
				}
			}

		}

		asset += landMoney * 0.7;

		return asset;
	}

	/**
	 * 이동 로직
	 * */
	public Player move(Player player, int move) {
		int currentLocation = player.getCurrentLocation();
		int nextLocation = (currentLocation + move) % 32;
		player.setCurrentLocation(nextLocation);
		System.out.println(player.getCurrentLocation());
		return player;

	}

	/**
	 * 이동 후 로직
	 * */
	public ResponseDto afterMove(String roomCode) {
		GameInfo gameInfo = getInfo(roomCode);
		Player[] players = gameInfo.getPlayers();
		int playerIdx = getPlayerIdx(players, gameInfo.getInfo().getCurrentPlayer());
		// 현재 플레이어
		Player curPlayer = players[playerIdx];
		logger.info("afterMove(), currentPlayer={}, currentLocation = {}",curPlayer.getNickname(),curPlayer.getCurrentLocation());
		int currentLocation = curPlayer.getCurrentLocation();
		/* 이동 후에 위치에 따라서 로직...*/
		/**/

		ResponseDto responseDto = null;
		switch (currentLocation) {
			case 0:
				// 시작지점
				logger.info("type=자유");
				responseDto = ResponseDto.builder().type("자유").data(gameInfo).build();
				break;
			case 2:
				// 세금 징수
				logger.info("type=세금징수");

				responseDto = ResponseDto.builder().type("세금징수").build();

				break;
			case 4:
			case 12:
			case 20:
			case 28:
				// 황금 열쇠
				logger.info("type=황금열쇠");

				responseDto = ResponseDto.builder().type("황금열쇠").build();
				break;
			case 8:
				// 거래정지
				logger.info("type=이동후턴종료");
				responseDto = ResponseDto.builder().type("이동후턴종료").build();
				break;
			case 10:
				// 박진호
				logger.info("type=박진호");
				responseDto = ResponseDto.builder().type("박진호").build();
				break;
			case 16:
				// 투자장
				logger.info("type=거래장");

				responseDto = ResponseDto.builder().type("거래장").build();
				break;
			case 18:
				// Rush & Cash
				logger.info("type=대출");
				responseDto = ResponseDto.builder().type("대출").build();
				break;
			case 24:
				// 어디로든 문
				logger.info("type=자유");
				responseDto = ResponseDto.builder().type("자유").data(gameInfo).build();

				break;
			default:
				// 일반 땅
				// 땅이 내 땅인지 중립땅인지 다른 플레이어의 땅인지 판별
				List<Land> lands = gameInfo.getLands();
				if (lands.get(currentLocation).getOwner() == -1) {
					// 중립 땅일 때
					logger.info("type=땅구매");

					responseDto = ResponseDto.builder().type("땅구매").build();
				} else if (lands.get(currentLocation).getOwner() == playerIdx) {
					// 내땅 일 때
					logger.info("type=건물구매");

					responseDto = ResponseDto.builder().type("건물구매").build();
				} else {
					// 다른 플레이어의 소유 일 때
					// 통행료 계산
					long fees = lands.get(currentLocation).getCurrentFees()[0];
					for (int i = 0; i < lands.get(currentLocation).getBuildings().length; i++) {
						if (lands.get(currentLocation).getBuildings()[i]) {
							fees += lands.get(currentLocation).getCurrentFees()[i + 1];
						}
					}
					long asset = calculateAsset(players[playerIdx], gameInfo.getStocks(), lands);
					if (fees <= players[playerIdx].getMoney()) {
						// 현금으로 통행료 지급이 가능할 때
						logger.info("type=통행료지급");

						responseDto = ResponseDto.builder().type("통행료지급").build();
					} else {
						// 현금으로 통행료 지급이 불가능 할 때
						if (asset >= fees) {
							// 매각할 수 있으면
							logger.info("type=매각");

							responseDto = ResponseDto.builder().type("매각").data(NeedSaleMoney.builder().needMoney(fees).build()).build();
						} else {
							// 파산각이면
							logger.info("type=파산");

							responseDto = ResponseDto.builder().type("파산").build();
						}
					}
				}
		}

		return responseDto;
	}

	/**
	 * 어디로든 문 (강준구의 문단속 적용 X)
	 * */
	public ResponseDto door(String roomCode, Door door) {
		GameInfo gameInfo = gameInfoRepository.getGameInfo(roomCode);
		Player[] players = gameInfo.getPlayers();
		int playerIdx = getPlayerIdx(players, gameInfo.getInfo().getCurrentPlayer());
		Player currentPlayer = players[playerIdx];
		// 현재 위치
		int currentLocation = currentPlayer.getCurrentLocation();
		// player가 지정한 위치
		int nextLocation = door.getLandId();

		// 1바퀴 돌았을 때
		if (currentLocation > nextLocation) {
			// 돈 바퀴 수 증가
			int currentLap = currentPlayer.getCurrentLap();
			currentPlayer.setCurrentLap(currentLap + 1);
			currentPlayer.setCurrentLocation(nextLocation);
			players[playerIdx] = currentPlayer;
			gameInfo.setPlayers(players);
			gameInfoRepository.createGameRoom(gameInfo);
			return ResponseDto.builder()
				.type("어디로든문맹맹지급")
				.data(DoorResponse.builder().lapCheck(true)
					.players(players)
					.build())
				.build();

		} else {
			currentPlayer.setCurrentLocation(nextLocation);
			players[playerIdx] = currentPlayer;
			gameInfo.setPlayers(players);
			gameInfoRepository.createGameRoom(gameInfo);
			return ResponseDto.builder()
				.type("어디로든문이동후로직")
				.data(DoorResponse.builder().lapCheck(false)
					.players(players)
					.build())
				.build();

		}
	}

	/**
	 * 어디로든 문 (강준구의 문단속 적용 O)
	 * */
	public ResponseDto junguDoor(String roomCode) {
		GameInfo gameInfo = gameInfoRepository.getGameInfo(roomCode);
		Player[] players = gameInfo.getPlayers();
		int playerIdx = getPlayerIdx(players, gameInfo.getInfo().getCurrentPlayer());
		Player currentPlayer = players[playerIdx];
		// 현재 위치
		int currentLocation = currentPlayer.getCurrentLocation();
		// 랜덤 위치
		Random random = new Random();

		// 랜덤으로 0부터 30까지의 수를 생성
		int randomNumber = random.nextInt(31);

		// 만약 생성된 수가 24라면 24를 제외하고 다시 랜덤 수를 생성
		while (randomNumber == 24) {
			randomNumber = random.nextInt(31);
		}
		int nextLocation = randomNumber;

		// 1바퀴 돌았을 때
		if (currentLocation > nextLocation) {
			// 돈 바퀴 수 증가
			int currentLap = currentPlayer.getCurrentLap();
			currentPlayer.setCurrentLap(currentLap + 1);
			currentPlayer.setCurrentLocation(nextLocation);
			players[playerIdx] = currentPlayer;
			gameInfo.setPlayers(players);
			gameInfoRepository.createGameRoom(gameInfo);
			return ResponseDto.builder()
				.type("어디로든문맹맹지급")
				.data(DoorResponse.builder().lapCheck(true)
					.players(players)
					.build())
				.build();

		} else {
			currentPlayer.setCurrentLocation(nextLocation);
			players[playerIdx] = currentPlayer;
			gameInfo.setPlayers(players);
			gameInfoRepository.createGameRoom(gameInfo);
			return ResponseDto.builder()
				.type("어디로든문이동후로직")
				.data(DoorResponse.builder().lapCheck(false)
					.players(players)
					.build())
				.build();

		}
	}

	/**
	 * 턴을 종료하는 로직
	 * 1. 현재 플레이어의 turnCount올리기
	 * 2. 다음 플레이어 확인 for 문 순회
	 * 3. waiting news 확인해서 effect news 적용
	 * 4.
	 * */
	public ResponseDto endTurn(String roomCode) {
		GameInfo gameInfo = gameInfoRepository.getGameInfo(roomCode);
		Player[] players = gameInfo.getPlayers();
		List<Land> lands = gameInfo.getLands();
		List<Stock> stocks = gameInfo.getStocks();
		int playerIdx = getPlayerIdx(players, gameInfo.getInfo().getCurrentPlayer());
		Player currentPlayer = players[playerIdx];
		logger.info("endTurn(), currentPlayer= {}, currentTurn = {}",currentPlayer.getNickname(),currentPlayer.getCurrentTurn());
		// 현재 플레이어의 turnCount++
		int currentTurn = currentPlayer.getCurrentTurn() + 1;
		currentPlayer.setCurrentTurn(currentTurn);
		// 현제 플레이어의 doubleCount 초기화
		currentPlayer.setDoubleCount(0);
		Info info = gameInfo.getInfo();
		// 턴을 넘기기 전에 현재 플레이어가 마지막 플레이어인지 확인.
		boolean isLastPlayer = false;
		// 배열의 마지막 사람이면 무조건 제일 마지막 플레이어
		if (info.getPlayerCnt() == playerIdx + 1) {
			isLastPlayer = true;
		} else {
			// 마지막 까지 배열을 순회하면서 살아있는 사람이 없으면 마지막 플레이어.
			for (int i = playerIdx; i < info.getPlayerCnt(); i++) {
				if (players[i].isAlive()) {
					isLastPlayer = false;
					break;
				}
			}
		}

		if (isLastPlayer) {
			//턴이 바뀔 때 수행되어야하는 로직
			int turnCount = info.getTurnCount() + 1;
			// 종료 조건 체크
			if (turnCount == 31) {
				return ResponseDto.builder().type("게임종료").build();
			}
			int doorCheck = info.getDoorCheck();
			if (doorCheck > 1) {
				doorCheck--;
			}
			// waitingNews가 비어있지 않을 때
			Queue<News> effectNews = info.getEffectNews();
			Queue<WaitingNews> waitingNews = info.getWaitingNews();
			while (!waitingNews.isEmpty()) {
				// 적용 시킬 뉴스가 있으면
				if (turnCount == waitingNews.peek().getTurn()) {
					// 이미 적용중인 뉴스가 3개라면 뉴스 한개 제거
					if (effectNews.size() == 3) {
						//TODO: 적용중인 뉴스 효과 제거도 해야함
						// TODO: 뉴스효과 뺄 때 뉴스 효과 빼고 나서 asset
						effectNews.poll();

					}
					effectNews.offer(waitingNews.poll().getNews());

					Map<Integer, Integer> effectedCountries = new HashMap<>();
					Map<Integer, Integer> effectedStocks = new HashMap<>();
					for (News curNews : effectNews) {
						effectedCountries.putAll(curNews.getCountryEffects());
						effectedStocks.putAll(curNews.getStockEffects());
					}

					//뉴스에 의해 변동 된 땅 값 초기화
					for (Map.Entry<Integer, Integer> entry : effectedCountries.entrySet()) {
						Land curLand = lands.get(entry.getKey());
						curLand.setCurrentBuildingPrices(curLand.getBuildingPrices());
						curLand.setCurrentLandPrice(curLand.getLandPrice());
						curLand.setCurrentFees(curLand.getFees());
					}

					//뉴스에 의해 변동 된 주식 가격 초기화
					for (Map.Entry<Integer, Integer> entry : effectedStocks.entrySet()) {
						Stock curStock = stocks.get(entry.getKey());
						curStock.setCurrentCost(curStock.getCost());
					}

					//현재 적용중인 뉴스 List를 돌면서 해당 뉴스가 끼치는 영향을 적용
					for (News curNews : effectNews) {
						Map<Integer, Integer> curEffectedCountries = curNews.getCountryEffects();
						Map<Integer, Integer> curEffectedStocks = curNews.getStockEffects();

						for (Map.Entry<Integer, Integer> entry : curEffectedCountries.entrySet()) {
							Land curLand = lands.get(entry.getKey());
							int curEffect = entry.getValue();

							int currentLandPrice = 0;
							int[] currentBuildingPrices = new int[3];
							int[] currentFees = new int[4];

							//좋은 효과
							if (curEffect >= 0) {
								currentLandPrice =
										curLand.getCurrentLandPrice() * (100 + curEffect) / 100;
								for (int i = 0; i < currentBuildingPrices.length; i++) {
									currentBuildingPrices[i] =
											curLand.getCurrentBuildingPrices()[i] * (100 + curEffect) / 100;
								}
								for (int i = 0; i < currentFees.length; i++) {
									currentFees[i] =
											curLand.getCurrentFees()[i] * (100 + curEffect) / 100;
								}
							}
							//나쁜 효과
							else {
								currentLandPrice =
										curLand.getCurrentLandPrice() * (100 - curEffect) / 100;
								for (int i = 0; i < currentBuildingPrices.length; i++) {
									currentBuildingPrices[i] =
											curLand.getCurrentBuildingPrices()[i] * (100 - curEffect) / 100;
								}
								for (int i = 0; i < currentFees.length; i++) {
									currentFees[i] =
											curLand.getCurrentFees()[i] * (100 - curEffect) / 100;
								}
							}
							//바뀐 정보 해당 땅에 업데이트
							curLand.setCurrentLandPrice(currentLandPrice);
							curLand.setCurrentBuildingPrices(currentBuildingPrices);
							curLand.setCurrentFees(currentFees);
							lands.set(entry.getKey(), curLand);
						}

						for (Map.Entry<Integer, Integer> entry : curEffectedStocks.entrySet()) {
							Stock curStocks = stocks.get(entry.getKey());
							int curEffect = entry.getValue();

							int currentCost = 0;

							//좋은 효과
							if (curEffect >= 0) {
								currentCost = curStocks.getCurrentCost() * (100 + curEffect) / 100;
							}
							//나쁜 효과
							else {
								currentCost = curStocks.getCurrentCost() * (100 - curEffect) / 100;
							}
							//바뀐 정보 해당 땅에 업데이트
							curStocks.setCurrentCost(currentCost);
							stocks.set(entry.getKey(), curStocks);
						}
					}

				} else {
					// 적용 시킬 뉴스 없으면
					break;
				}
			}
			info.setDoorCheck(doorCheck);
			info.setTurnCount(turnCount);
			info.setEffectNews(effectNews);
			info.setWaitingNews(waitingNews);

			logger.info("다음턴={}, 문단속 적용={}",turnCount,doorCheck);

		}

		// 다음 플레이어
		int nextPlayerIdx = -1;
		// 다음 플레이어 idx 구하는 로직
		for (int i = 1; i < 4; i++) {
			int idx = (playerIdx + i) % 4;
			if (players[idx] != null && players[idx].isAlive()) {
				nextPlayerIdx = idx;
				break;
			}

		}
		if (nextPlayerIdx != -1) {
			// 다음 플레이어 순서를 설정
			info.setCurrentPlayer(players[nextPlayerIdx].getNickname());
		}
		gameInfo.setLands(lands);
		gameInfo.setStocks(stocks);
		gameInfo.setPlayers(players);
		gameInfo.setInfo(info);
		gameInfoRepository.createGameRoom(gameInfo);

		logger.info("다음플레이어={}",players[nextPlayerIdx].getNickname());
		logger.info("type=턴종료끝");

		return ResponseDto.builder().type("턴종료끝")
			.data(EndTurnResponse.builder()
				.info(info)
				.stocks(gameInfo.getStocks())
				.players(players)
				.lands(gameInfo.getLands())
				.build())
			.build();
	}

	/**
	 * 게임 종료
	 * */
	public ResponseDto endGame(String roomCode) {
		//TODO: 게임 종료시 데이터 전송

		return ResponseDto.builder().type("게임결과").build();

	}

	/**
	 * 선택한 뉴스 적용
	 */
	public ResponseDto applyNews(String roomCode, News news, String type) {
		// 게임 정보 가져오기
		GameInfo gameInfo = gameInfoRepository.getGameInfo(roomCode);
		List<Land> lands = gameInfo.getLands();
		List<Stock> stocks = gameInfo.getStocks();
		Info info = gameInfo.getInfo();

		Queue<News> effectNews = info.getEffectNews();
		Queue<WaitingNews> waitingNews = info.getWaitingNews();

		if (!type.equals("다이아몬드")) {
			if (effectNews.size() == 3) {
				effectNews.poll();
			}
			effectNews.add(news);
			//땅 값 바뀌면 주식과 플레이어 총 자산이 변경

			Map<Integer, Integer> effectedCountries = new HashMap<>();
			Map<Integer, Integer> effectedStocks = new HashMap<>();
			for (News curNews : effectNews) {
				effectedCountries.putAll(curNews.getCountryEffects());
				effectedStocks.putAll(curNews.getStockEffects());
			}

			//뉴스에 의해 변동 된 땅 값 초기화
			for (Map.Entry<Integer, Integer> entry : effectedCountries.entrySet()) {
				Land curLand = lands.get(entry.getKey());
				curLand.setCurrentBuildingPrices(curLand.getBuildingPrices());
				curLand.setCurrentLandPrice(curLand.getLandPrice());
				curLand.setCurrentFees(curLand.getFees());
			}

			//뉴스에 의해 변동 된 주식 가격 초기화
			for (Map.Entry<Integer, Integer> entry : effectedStocks.entrySet()) {
				Stock curStock = stocks.get(entry.getKey());
				curStock.setCurrentCost(curStock.getCost());
			}

			//현재 적용중인 뉴스 List를 돌면서 해당 뉴스가 끼치는 영향을 적용
			for (News curNews : effectNews) {
				Map<Integer, Integer> curEffectedCountries = curNews.getCountryEffects();
				Map<Integer, Integer> curEffectedStocks = curNews.getStockEffects();

				for (Map.Entry<Integer, Integer> entry : curEffectedCountries.entrySet()) {
					Land curLand = lands.get(entry.getKey());
					int curEffect = entry.getValue();

					int currentLandPrice = 0;
					int[] currentBuildingPrices = new int[3];
					int[] currentFees = new int[4];

					//좋은 효과
					if (curEffect >= 0) {
						currentLandPrice =
							curLand.getCurrentLandPrice() * (100 + curEffect) / 100;
						for (int i = 0; i < currentBuildingPrices.length; i++) {
							currentBuildingPrices[i] =
								curLand.getCurrentBuildingPrices()[i] * (100 + curEffect) / 100;
						}
						for (int i = 0; i < currentFees.length; i++) {
							currentFees[i] =
								curLand.getCurrentFees()[i] * (100 + curEffect) / 100;
						}
					}
					//나쁜 효과
					else {
						currentLandPrice =
							curLand.getCurrentLandPrice() * (100 - curEffect) / 100;
						for (int i = 0; i < currentBuildingPrices.length; i++) {
							currentBuildingPrices[i] =
								curLand.getCurrentBuildingPrices()[i] * (100 - curEffect) / 100;
						}
						for (int i = 0; i < currentFees.length; i++) {
							currentFees[i] =
								curLand.getCurrentFees()[i] * (100 - curEffect) / 100;
						}
					}
					//바뀐 정보 해당 땅에 업데이트
					curLand.setCurrentLandPrice(currentLandPrice);
					curLand.setCurrentBuildingPrices(currentBuildingPrices);
					curLand.setCurrentFees(currentFees);
					lands.set(entry.getKey(), curLand);
				}

				for (Map.Entry<Integer, Integer> entry : curEffectedStocks.entrySet()) {
					Stock curStocks = stocks.get(entry.getKey());
					int curEffect = entry.getValue();

					int currentCost = 0;

					//좋은 효과
					if (curEffect >= 0) {
						currentCost = curStocks.getCurrentCost() * (100 + curEffect) / 100;
					}
					//나쁜 효과
					else {
						currentCost = curStocks.getCurrentCost() * (100 - curEffect) / 100;
					}
					//바뀐 정보 해당 땅에 업데이트
					curStocks.setCurrentCost(currentCost);
					stocks.set(entry.getKey(), curStocks);
				}
			}
		} else {
			waitingNews.add(
				WaitingNews.builder().news(news).turn(info.getTurnCount() + 3).build());
		}

		info.setEffectNews(effectNews);
		info.setWaitingNews(waitingNews);

		gameInfo.setLands(lands);
		gameInfo.setStocks(stocks);
		gameInfo.setInfo(info);

		gameInfoRepository.createGameRoom(gameInfo);

		return ResponseDto.builder()
			.type("자유")
			.data(gameInfo)
			.build();
	}

	/**
	 * 파산
	 */
	public ResponseDto bankruptcy(String roomCode) {
		GameInfo gameInfo = getInfo(roomCode);
		Player[] players = gameInfo.getPlayers();
		List<Land> lands = gameInfo.getLands();

		String currentPlayer = gameInfo.getInfo().getCurrentPlayer();
		int currentIdx = -1;
		// 현재 플레이어 인덱스 찾기
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null && players[i].isAlive() && players[i].getNickname().equals(currentPlayer)) {
				currentIdx = i;
			}
		}
		Player curPlayer = players[currentIdx];

		//플레이어 사망 처리
		curPlayer.setAlive(false);
		players[currentIdx] = curPlayer;

		for (Land land : lands) {
			if (land.getOwner() == currentIdx) {
				land.setCurrentFees(land.getFees());
				land.setCurrentBuildingPrices(land.getBuildingPrices());
				land.setCurrentLandPrice(land.getLandPrice());
				land.setBuildings(new boolean[] {false, false, false});
				land.setOwner(-1);

				lands.set(land.getLandId(), land);
			}
		}

		curPlayer.setLands(new ArrayList<>());

		int aliveCnt = 0;
		for (int i = 0; i < players.length; i++) {
			if (players[i].isAlive()) {
				aliveCnt++;
			}
		}

		gameInfo.setPlayers(players);
		gameInfo.setLands(lands);

		gameInfoRepository.createGameRoom(gameInfo);

		ResponseDto responseDto = ResponseDto.builder().build();

		if(aliveCnt == 1) {
			responseDto = ResponseDto.builder().type("게임종료").build();
		} else {
			responseDto = ResponseDto.builder().type("파산턴종료").data(BankruptcyResponse.builder().lands(lands).players(players).build()).build();
		}

		return responseDto;
	}
}
