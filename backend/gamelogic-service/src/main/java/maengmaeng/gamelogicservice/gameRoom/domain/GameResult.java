package maengmaeng.gamelogicservice.gameRoom.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int gameResultId;
    private String userId;
    private int rating;
    private int asset;
    private int landAmount;
    private int stockAmount;
    private int stockAsset;
    private int loanNum;
    private int doorUsedNum;
    private int keyUsedNum;
    private int angelUsedNum;
    private int survivalTurn;
}
