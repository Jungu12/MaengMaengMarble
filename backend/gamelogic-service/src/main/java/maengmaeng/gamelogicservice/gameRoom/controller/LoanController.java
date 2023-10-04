package maengmaeng.gamelogicservice.gameRoom.controller;

import lombok.AllArgsConstructor;
import maengmaeng.gamelogicservice.gameRoom.domain.GameInfo;
import maengmaeng.gamelogicservice.gameRoom.domain.Player;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.LoanRequest;
import maengmaeng.gamelogicservice.gameRoom.domain.dto.LoanResponse;
import maengmaeng.gamelogicservice.gameRoom.service.GameRoomService;
import maengmaeng.gamelogicservice.gameRoom.service.LoanService;
import maengmaeng.gamelogicservice.global.dto.GameData;
import maengmaeng.gamelogicservice.global.dto.ResponseDto;
import maengmaeng.gamelogicservice.util.RedisPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class LoanController {

    private final LoanService loanService;
    private final GameRoomService gameRoomService;
    private final RedisPublisher redisPublisher;
    private final ChannelTopic gameRoomTopic;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @MessageMapping("/game-rooms/loan/borrow/{roomCode}")
    public void borrow(@DestinationVariable String roomCode, LoanRequest loanRequest){
        loanService.borrow(roomCode, loanRequest);

        GameInfo gameInfo  = gameRoomService.getInfo(roomCode);

        Player changedPlayer = null;

        for(Player player : gameInfo.getPlayers()){
            if(player.getNickname().equals(gameInfo.getInfo().getCurrentPlayer())){
                changedPlayer = player;
                break;
            }
        }



        GameData gameData = GameData.builder()
                .type("GAME_ROOM")
                .roomCode(roomCode)
                .data(ResponseDto.builder().type("대출 완료").data(LoanResponse.builder().player(changedPlayer).build()).build())
                .build();
        redisPublisher.publish(gameRoomTopic,gameData);
    }

    @MessageMapping("/game-rooms/loan/repay/{roomCode}")
    public void repay(@DestinationVariable String roomCode, LoanRequest loanRequest){
        loanService.repay(roomCode, loanRequest);


        GameInfo gameInfo  = gameRoomService.getInfo(roomCode);

        Player changedPlayer = null;

        for(Player player : gameInfo.getPlayers()){
            if(player.getNickname().equals(gameInfo.getInfo().getCurrentPlayer())){
                changedPlayer = player;
                break;
            }
        }


        GameData gameData = GameData.builder()
                .type("GAME_ROOM")
                .roomCode(roomCode)
                .data(ResponseDto.builder().type("대출 갚기 완료").data(LoanResponse.builder().player(changedPlayer).build()).build())
                .build();
        redisPublisher.publish(gameRoomTopic,gameData);
    }
}
