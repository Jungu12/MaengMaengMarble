package maengmaeng.gamelogicservice.lobby.controller;

import lombok.RequiredArgsConstructor;
import maengmaeng.gamelogicservice.lobby.service.LobbyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/maeng")
public class LobbyController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final LobbyService lobbyService;

    // 로비 구독
    @GetMapping("/lobby/rooms")
    @ResponseBody
    public ResponseEntity<?> enterLobby(@AuthenticationPrincipal String userId) {
        logger.debug("lobby(), userId = {}", userId);

        lobbyService.enterLobby(userId);
        return ResponseEntity.ok().build();
    }

}
