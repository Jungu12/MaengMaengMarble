package maengmaeng.userservice.shop.controller;

import lombok.AllArgsConstructor;
import maengmaeng.userservice.shop.domain.dto.AvatarResponseDto;
import maengmaeng.userservice.shop.service.ShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@AllArgsConstructor
@RestController
@RequestMapping("/api/shop")
public class ShopController {

    private final ShopService shopService;

    @GetMapping("/list/{loginUser}")
    public ResponseEntity<List<AvatarResponseDto>> getAvatars(@PathVariable String loginUser){
        List<AvatarResponseDto> avatarList = shopService.getAvatars(loginUser);
        return ResponseEntity.ok(avatarList);
    }

    @PostMapping("/characters/{id}/{loginUser}")
    public ResponseEntity<Void> buyAvatar(@PathVariable String loginUser, @PathVariable int id){
        shopService.buyAvatar(loginUser, id);
        return ResponseEntity.ok().build();
    }
}
