package maengmaeng.gamelogicservice.gameRoom.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import maengmaeng.gamelogicservice.gameRoom.domain.GameResult;
import maengmaeng.gamelogicservice.gameRoom.domain.Player;
import maengmaeng.gamelogicservice.gameRoom.domain.Stock;
import maengmaeng.gamelogicservice.gameRoom.repository.GameInfoRepository;
import maengmaeng.gamelogicservice.gameRoom.repository.GameResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class GameResultService {
    private static final String GAME_RESULT = "gameResult";

    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, Object> hashOperations;
    private final GameResultRepository gameResultRepository;

    @Autowired
    public GameResultService(RedisTemplate<String, Object> redisTemplate, GameResultRepository gameResultRepository) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
        this.gameResultRepository = gameResultRepository;
    }

    @Transactional
    public void saveGameResultsFromRedis(String roomCode) {
        // Redis에서 해당 방(roomCode)의 모든 플레이어 데이터를 가져옵니다.
        Set<String> playerKeys = redisTemplate.keys(roomCode + "_*");

        // 모든 플레이어 데이터를 처리하여 하나의 GameResult 객체로 합칩니다.
        GameResult combinedGameResult = new GameResult();

        for (String playerKey : playerKeys) {
            String nickname = playerKey.substring(roomCode.length() + 1); // 플레이어의 nickname 추출

            // 각 플레이어의 정보를 가져와서 combinedGameResult에 추가합니다.
            GameResult playerGameResult = getPlayerDataFromRedis(roomCode, nickname);

            // 각 플레이어의 정보를 combinedGameResult에 병합합니다.
            combinedGameResult.setUserId(playerGameResult.getUserId());
            combinedGameResult.setRating(combinedGameResult.getRating() + playerGameResult.getRating());
            combinedGameResult.setAsset(combinedGameResult.getAsset() + playerGameResult.getAsset());
            combinedGameResult.setLandAmount(combinedGameResult.getLandAmount() + playerGameResult.getLandAmount());
            combinedGameResult.setStockAmount(combinedGameResult.getStockAmount() + playerGameResult.getStockAmount());
            combinedGameResult.setStockAsset(combinedGameResult.getStockAsset() + playerGameResult.getStockAsset());
            combinedGameResult.setLoanNum(combinedGameResult.getLoanNum() + playerGameResult.getLoanNum());
            combinedGameResult.setDoorUsedNum(combinedGameResult.getDoorUsedNum() + playerGameResult.getDoorUsedNum());
            combinedGameResult.setKeyUsedNum(combinedGameResult.getKeyUsedNum() + playerGameResult.getKeyUsedNum());
            combinedGameResult.setAngelUsedNum(combinedGameResult.getAngelUsedNum() + playerGameResult.getAngelUsedNum());
            combinedGameResult.setSurvivalTurn(Math.max(combinedGameResult.getSurvivalTurn(), playerGameResult.getSurvivalTurn()));

            // DB에 하나의 합친 GameResult 객체를 저장합니다.
            gameResultRepository.save(combinedGameResult);
        }
    }

    @Transactional
    public GameResult getPlayerDataFromRedis(String roomCode, String nickname) {
        String playerKey = roomCode + "_" + nickname;
        GameResult gameResult = new GameResult();
        gameResult.setUserId((String) hashOperations.get(GAME_RESULT, playerKey + "_userId"));
        gameResult.setRating((int) hashOperations.get(GAME_RESULT, playerKey + "_rating"));
        gameResult.setAsset((int) hashOperations.get(GAME_RESULT, playerKey + "_asset"));
        gameResult.setLandAmount((int) hashOperations.get(GAME_RESULT, playerKey + "_landAmount"));
        gameResult.setStockAmount((int) hashOperations.get(GAME_RESULT, playerKey + "_stockAmount"));
        gameResult.setStockAsset((int) hashOperations.get(GAME_RESULT, playerKey + "_stockAsset"));
        gameResult.setLoanNum((int) hashOperations.get(GAME_RESULT, playerKey + "_loanNum"));
        gameResult.setDoorUsedNum((int) hashOperations.get(GAME_RESULT, playerKey + "_doorUsedNum"));
        gameResult.setKeyUsedNum((int) hashOperations.get(GAME_RESULT, playerKey + "_keyUsedNum"));
        gameResult.setAngelUsedNum((int) hashOperations.get(GAME_RESULT, playerKey + "_angelUsedNum"));
        gameResult.setSurvivalTurn((int) hashOperations.get(GAME_RESULT, playerKey + "_survivalTurn"));

        // LandAmount 계산
        int landAmount = ((List<?>) hashOperations.get(GAME_RESULT, playerKey + "_lands")).size();
        gameResult.setLandAmount(landAmount);

        // StockAmount 계산
        List<Integer> stocks = (List<Integer>) hashOperations.get(GAME_RESULT, playerKey + "_stocks");
        int stockAmount = calculateStockAmount(stocks);
        gameResult.setStockAmount(stockAmount);

        // StockAsset 계산
        List<Integer> stockIndex = (List<Integer>) hashOperations.get(GAME_RESULT, playerKey + "_stocksIndex");
        List<Stock> gameStocks = (List<Stock>) hashOperations.get(GAME_RESULT, "stocks");
        int stockAsset = calculateStockAsset(stocks, stockIndex, gameStocks);
        gameResult.setStockAsset(stockAsset);

        // rating 계산
        int rating = calculateRating(playerKey);
        gameResult.setRating(rating);

        return gameResult;
    }

    private int calculateStockAmount(List<Integer> stocks) {
        // StockAmount 계산: stocks 배열에서 0이 아닌 값의 개수
        int count = 0;
        for (Integer stock : stocks) {
            if (stock != 0) {
                count++;
            }
        }
        return count;
    }

    private int calculateStockAsset(List<Integer> stocks, List<Integer> stocksIndex, List<Stock> gameStocks) {
        // StockAsset 계산: stocks 배열에서 0이 아닌 값의 인덱스에 해당하는 currentCost와 곱한 값
        int asset = 0;
        for (int i = 0; i < stocks.size(); i++) {
            if (stocks.get(i) != 0) {
                int currentCost = gameStocks.get(stocksIndex.get(i) - 1).getCurrentCost(); // gameStocks에서 해당 인덱스의 currentCost 가져오기
                asset += currentCost * stocks.get(i);
            }
        }
        return asset;
    }

    private int calculateRating(String playerKey) {
        // Rating 계산 로직: currentTurn이 클수록 높은 rating, 같다면 asset이 큰 순서대로 rating 매김
        int currentTurn = (int) hashOperations.get(GAME_RESULT, playerKey + "_currentTurn");
        int asset = (int) hashOperations.get(GAME_RESULT, playerKey + "_asset");

        int rating = 0;

        // 순위 계산
        for (String key : hashOperations.keys(GAME_RESULT)) {
            if (key.endsWith("_currentTurn") && key.endsWith("_asset")) {
                int otherPlayerCurrentTurn = (int) hashOperations.get(GAME_RESULT, key);
                int otherPlayerAsset = (int) hashOperations.get(GAME_RESULT, key.replace("_currentTurn", "_asset"));

                // 순위 비교
                if ((otherPlayerCurrentTurn > currentTurn) || (otherPlayerCurrentTurn == currentTurn && otherPlayerAsset > asset)) {
                    rating++;
                }
            }
        }
        rating += 1;

        return rating;
    }
}
