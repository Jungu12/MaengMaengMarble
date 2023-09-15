package maengmaeng.userservice.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maengmaeng.userservice.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 내 정보를 반환한다.
     * @return
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> users(@PathVariable String userId) {
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
     * @param newNickname
     * @return
     */
    @PatchMapping("/{userId}/nicknames")
    public ResponseEntity<?> changeNickname(@PathVariable String userId, @RequestParam String newNickname) {
        logger.debug("NicknameChange(), userId = {}, newnickname = {}", userId, newNickname);
        userService.changeNickname(userId, newNickname);
        return ResponseEntity.ok().build();
    }

    /**
     * 내가 보유한 캐릭터 조회
     * @param userId
     * @return
     */
    @GetMapping("/{userId}/avatars")
    public ResponseEntity<?> avatarList(@PathVariable String userId) {
        logger.debug("avatarList(), userId = {}", userId);

        return ResponseEntity.ok().body(userService.getMyAvatars(userId));
    }

    /**
     * 캐릭터 변경
     * @param userId
     * @param newAvatarId
     * @return
     */
    @PatchMapping("/{userId}/avatars/{newAvatarId}")
    public ResponseEntity<?> changeAvatar(@PathVariable String userId, @PathVariable int newAvatarId) {
        logger.debug("changeAvatar(), userId = {}, newAvatarId = {}", userId, newAvatarId);
        userService.changeProfileAvatar(userId, newAvatarId);

        return ResponseEntity.ok().build();
    }
//
//    /**
//     * 닉네임 중복 확인
//     * @param nickname
//     * @return
//     */
//    @GetMapping("/nicknames/duplication")
//    public ResponseEntity<?> checkNicknameDuplication(@RequestParam("nickname") String nickname) {
//        logger.debug("NicknameDuplicationCheck(), nickname = {}", nickname);
//
//        return ResponseEntity.ok().body(userService.isNicknameDuplicated(nickname));
//    }
//
//    /**
//     * 닉네임 변경
//     * @param userId
//     * @param newNickname
//     * @return
//     */
//    @PatchMapping("/nicknames")
//    public ResponseEntity<?> changeNickname(@AuthenticationPrincipal String userId, String newNickname) {
//        logger.debug("NicknameChange(), userId = {}, newnickname = {}", userId, newNickname);
//        userService.changeNickname(userId, newNickname);
//
//        return ResponseEntity.ok().build();
//    }
//
//    /**
//     * 내가 보유한 캐릭터 조회
//     * @param userId
//     * @return
//     */
//    @GetMapping("/avatars")
//    public ResponseEntity<?> avatarList(@AuthenticationPrincipal String userId) {
//        logger.debug("avatarList(), userId = {}", userId);
//
//        return ResponseEntity.ok().body(userService.getMyAvatars(userId));
//    }
//
//    /**
//     * 캐릭터 변경
//     * @param userId
//     * @param newAvatarId
//     * @return
//     */
//    @PatchMapping("/avatars/{newAvatarId}")
//    public ResponseEntity<?> changeAvatar(@AuthenticationPrincipal String userId, @PathVariable int newAvatarId) {
//        logger.debug("changeAvatar(), userId = {}, newAvatarId = {}", userId, newAvatarId);
//        userService.changeProfileAvatar(userId, newAvatarId);
//
//        return ResponseEntity.ok().build();
//    }
}
