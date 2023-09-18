package maengmaeng.userservice.signIn.controller;

import lombok.RequiredArgsConstructor;
import maengmaeng.userservice.signIn.service.SignInService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user-service/singin")
public class SignInController {
    private static SignInService signInService;
    @PostMapping("/nicknames")
    ResponseEntity<Void> changeNickname(@AuthenticationPrincipal String loginUser, @RequestBody Map<String,String> nickname){
        signInService.changeNickname(loginUser, nickname.get("nickname"));
        return ResponseEntity.ok().build();
    }
}
