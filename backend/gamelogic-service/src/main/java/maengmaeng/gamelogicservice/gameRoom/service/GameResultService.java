package maengmaeng.gamelogicservice.gameRoom.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import maengmaeng.gamelogicservice.gameRoom.domain.GameResult;
import maengmaeng.gamelogicservice.gameRoom.repository.GameResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GameResultService {

    @Autowired
    private StringRedisTemplate redisTemplate; // redis 연결

    @Autowired
    private GameResultRepository gameResultRepository;

    public String getGameResultFromRedis(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public GameResult convertJsonToGameResult(String jsonData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonData, GameResult.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void processGameResultFromRedis(String redisKey) {
        try {
            String jsonData = getGameResultFromRedis(redisKey);

            if (jsonData != null) {
                GameResult gameResult = convertJsonToGameResult(jsonData);

                if (gameResult != null) {
                    String userId = gameResult.getUserId();
                    int rating = gameResult.getRating();
                    int asset = gameResult.getAsset();
                    int landAmount = gameResult.getLandAmount();
                    int stockAmount = gameResult.getStockAmount();
                    int stockAsset = gameResult.getStockAsset();
                    int loanNum = gameResult.getLoanNum();
                    int doorUsedNum = gameResult.getDoorUsedNum();
                    int keyUsedNum = gameResult.getKeyUsedNum();
                    int angelUsedNum = gameResult.getAngelUsedNum();
                    int survivalTurn = gameResult.getSurvivalTurn();

                    saveDataToDatabase(userId, rating, asset, landAmount, stockAmount, stockAsset, loanNum, doorUsedNum, keyUsedNum, angelUsedNum, survivalTurn);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveDataToDatabase(String userId, int rating,
                                   int asset, int landAmount,
                                   int stockAmount, int stockAsset,
                                   int loanNum, int doorUsedNum,
                                   int keyUsedNUm, int angelUsedNum,
                                   int survivalTurn) {
        GameResult gameResult = GameResult.builder()
                .userId(userId).rating(rating).asset(asset)
                .landAmount(landAmount).stockAmount(stockAmount)
                .stockAsset(stockAsset).loanNum(loanNum)
                .doorUsedNum(doorUsedNum).keyUsedNum(keyUsedNUm)
                .angelUsedNum(angelUsedNum).survivalTurn(survivalTurn).build();
        gameResultRepository.save(gameResult);
    }
}
