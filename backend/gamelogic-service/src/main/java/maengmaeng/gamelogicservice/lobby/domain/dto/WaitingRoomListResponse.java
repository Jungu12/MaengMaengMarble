package maengmaeng.gamelogicservice.lobby.domain.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import maengmaeng.gamelogicservice.waitingRoom.domain.WaitingRoom;

@Builder
@Getter
public class WaitingRoomListResponse {
	List<WaitingRoom> waitingRooms;
}
