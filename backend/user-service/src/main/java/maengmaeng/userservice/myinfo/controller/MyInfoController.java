package maengmaeng.userservice.myinfo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maengmaeng.userservice.myinfo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/myInfo")
@RequiredArgsConstructor
@Slf4j
public class MyInfoController {

    private UserService userService;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 내 정보를 반환한다.
     * @param userId
     * @return
     */
    @GetMapping("/users")
    public ResponseEntity<?> users(@AuthenticationPrincipal String userId) {
        // 제대로 user_id를 가져오느냐 확인하기 위한 로그
        logger.debug("users(), userId = {}", userId);

        return ResponseEntity.ok().body(userService.findUser(userId));
    }

    /**
     * 닉네임 중복 확인
     * @param nickname
     * @return
     */
    @GetMapping("/nicknames/duplication")
    public ResponseEntity<?> checkNicknameDuplication(@RequestParam("nickname") String nickname) {
        logger.debug("NicknameDuplicationCheck(), nickname = {}", nickname);

        return ResponseEntity.ok().body(userService.isNicknameDuplicated(nickname));
    }

    /**
     * 닉네임 변경
     * @param userId
     * @param newNickname
     * @return
     */
    @PatchMapping("/nicknames")
    public ResponseEntity<?> changeNickname(@AuthenticationPrincipal String userId, String newNickname) {
        logger.debug("NicknameChange(), userId = {}, newnickname = {}", userId, newNickname);
        userService.changeNickname(userId, newNickname);

        return ResponseEntity.ok().build();
    }
}
