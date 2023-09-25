package maengmaeng.userservice.shop.controller;

import lombok.AllArgsConstructor;
import maengmaeng.userservice.shop.domain.dto.AvatarResponseDto;
import maengmaeng.userservice.shop.domain.dto.ShopResponseDto;
import maengmaeng.userservice.shop.service.ShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;


@AllArgsConstructor
@RestController
@RequestMapping("/api/user-serivce/shop")
public class ShopController {

    private final ShopService shopService;

    @GetMapping("/list")
    public ResponseEntity<ShopResponseDto> getAvatars(@AuthenticationPrincipal String loginUser){
        ShopResponseDto avatarList = shopService.getAvatars(loginUser);
        return ResponseEntity.ok(avatarList);
    }

    @PostMapping("/characters")
    public ResponseEntity<ShopResponseDto> buyAvatar(@AuthenticationPrincipal String loginUser, @RequestBody Map<String,Integer> id){
        ShopResponseDto avatarList = shopService.buyAvatar(loginUser, id.get("id"));
        return ResponseEntity.ok(avatarList);
    }
}