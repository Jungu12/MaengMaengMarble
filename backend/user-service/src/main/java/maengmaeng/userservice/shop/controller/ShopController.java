package maengmaeng.userservice.shop.controller;

import lombok.AllArgsConstructor;
import maengmaeng.userservice.shop.domain.dto.AvatarResponseDto;
import maengmaeng.userservice.shop.domain.dto.ShopResponseDto;
import maengmaeng.userservice.shop.service.ShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;


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

    @PostMapping("/characters/{id}")
    public ResponseEntity<Void> buyAvatar(@AuthenticationPrincipal String loginUser, @PathVariable int id){
        shopService.buyAvatar(loginUser, id);
        return ResponseEntity.ok().build();
    }
}
