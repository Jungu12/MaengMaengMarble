package maengmaeng.gamelogicservice.gameRoom.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import maengmaeng.gamelogicservice.gameRoom.domain.GameInfo;
import maengmaeng.gamelogicservice.gameRoom.service.GameRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TestController {

    private final GameRoomService gameRoomService;



    @RequestMapping("test")
    public ResponseEntity<?> test(){

        GameInfo info = gameRoomService.setInfo();
        return ResponseEntity.ok().body(info);

    }
}
