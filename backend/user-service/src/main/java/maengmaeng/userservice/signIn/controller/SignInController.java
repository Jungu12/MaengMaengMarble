package maengmaeng.userservice.signIn.controller;

import lombok.RequiredArgsConstructor;
import maengmaeng.userservice.signIn.service.SignInService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/signIn")
public class SignInController {
    private final SignInService signInService;
    @PostMapping("/nicknames")
    ResponseEntity<Void> changeNickname(@AuthenticationPrincipal String loginUser, @RequestBody Map<String,String> nickname){
        System.out.println(loginUser);
        signInService.changeNickname(loginUser, nickname.get("nickname"));
        return ResponseEntity.ok().build();
    }
}
