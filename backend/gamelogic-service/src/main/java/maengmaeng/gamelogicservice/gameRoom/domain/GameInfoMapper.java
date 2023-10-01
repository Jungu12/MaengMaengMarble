package maengmaeng.gamelogicservice.gameRoom.domain;

import maengmaeng.gamelogicservice.gameRoom.domain.db.*;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GameInfoMapper {

	/**
	 * redisPlayer객체로 전환하는 Mapper
	 * */
	public Player toReidsPlayer(String id, String nickName, int avatarId, String avatarImage) {
		boolean[] cards = {false, false};
		return Player.builder()
			.playerId(id)
			.avatarId(avatarId)
			.avatarImage(avatarImage)
			.nickname(nickName)
			.money(100000000)
			.asset(100000000)
			.lands(new ArrayList<Integer>())
			.alive(true)
			.currentTurn(1)
			.stopTradeCount(0)
			.doubleCount(0)
			.currentLap(0)
			.stocks(new int[15])
			.loan(0)
			.cards(cards)
			.currentLocation(0)
			.build();
	}

	/** DB Country객체 REDIS 에 저장할 Land 객체로 전환
	 * */
	public Land toRedisLand(DbCountry dbCountry) {
		int[] buildingPrices = {dbCountry.getVillaPrice(), dbCountry.getBuildingPrice(), dbCountry.getHotelPrice()};
		int[] fees = {dbCountry.getLandFee(), dbCountry.getVillaFee(), dbCountry.getBuildingFee(),
			dbCountry.getHotelPrice()};
		boolean[] buildings = {false, false, false};

		return Land.builder()
			.landId(dbCountry.getCountryId())
			.name(dbCountry.getCountryName())
			.landPrice(dbCountry.getLandPrice())
			.buildingPrices(buildingPrices)
			.fees(fees)
			.currentLandPrice(dbCountry.getLandPrice())
			.currentBuildingPrices(buildingPrices)
			.buildings(buildings)
			.currentFees(fees)
			.owner(-1)
			.build();

	}

	/**
	 * redis Info 객체로 변환하는 Mapper
	 * */
	public Info toRedisInfo(String currentPlayer, int turnCount) {

		return Info.builder()
			.currentPlayer(currentPlayer)
			.turnCount(turnCount)
			.effectNews(new ArrayDeque<>())
			.waitingNews(new ArrayDeque<>())
			.build();
	}

	/**
	 * redis GoldenKeys 객체로 변환하는  Mapper
	 * */
	public GoldenKeys toRedisGoldenKeys(int bronze, int silver, int platinum) {

		return GoldenKeys.builder()
			.bronze(bronze)
			.silver(silver)
			.platinum(platinum)
			.newsBan(4)
			.hurricane(4)
			.angel(4)
			.kangJunGu(2)
			.lotto(2)
			.door(2)
			.earthquake(4)
			.build();
	}

	/** DBNews 객체 REDIS에 저장할 news 객체로 전환
	 * */
	public News toRedisNews(DbNews dbNews) {

		List<DbNewsCountry> dbNewsCountryList = dbNews.getDbNewsCountryList();
		List<Map<String, Integer>> countryEffects = new ArrayList<>();

		List<DbNewsStock> dbNewsStockList = dbNews.getDbNewsStockList();
		List<Map<String, Integer>> stockEffects = new ArrayList<>();

		for (DbNewsCountry dbNewsCountry : dbNewsCountryList) {
			Map<String, Integer> countryEffect = new HashMap<>();
			countryEffect.put(dbNewsCountry.getDbCountry().getCountryName(), dbNewsCountry.getEffect());
			countryEffects.add(countryEffect);
		}
		for (DbNewsStock dbNewsStock : dbNewsStockList) {
			Map<String, Integer> stockEffect = new HashMap<>();
			stockEffect.put(dbNewsStock.getDbStock().getStockName(), dbNewsStock.getEffect());
			stockEffects.add(stockEffect);

		}
		return News.builder()
			.newsId(dbNews.getNewsId())
			.imageUrl(dbNews.getNewsImage())
			.content(dbNews.getNewsContent())
			.countryEffects(countryEffects)
			.stockEffects(stockEffects)
			.build();
	}

	/**
	 *  Redis Stock Mapper
	 * */
	public Stock toRedisStock(DbStock dbStock) {

		return Stock.builder()
			.id(dbStock.getStockId())
			.name(dbStock.getStockName())
			.cost(dbStock.getStockPrice())
			.currentCost(dbStock.getStockPrice())
			.dividends(dbStock.getDividends())
			.build();
	}

}
