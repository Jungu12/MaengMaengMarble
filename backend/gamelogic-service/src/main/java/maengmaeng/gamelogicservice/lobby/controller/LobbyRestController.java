package maengmaeng.gamelogicservice.lobby.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import maengmaeng.gamelogicservice.lobby.service.LobbyService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/lobby")
public class LobbyRestController {
	private final LobbyService lobbyService;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@GetMapping("/rooms")
	public ResponseEntity<?> waitingRoomCreate() {
		logger.debug("waitingRoomCreate()");

		return ResponseEntity.ok().body(lobbyService.createWaitingRoom());
	}
}
