package maengmaeng.userservice.relation.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maengmaeng.userservice.myinfo.domain.User;
import maengmaeng.userservice.relation.domain.Relation;
import maengmaeng.userservice.relation.service.RelationService;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user-service/relation")
public class RelationController {

    private final RelationService relationService;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    /*
        from : 로그인된 사용자, 친구 요청 보내는 사람
        to : 내가 친구요청 하는 사람
     */
    @PostMapping("/relations")
    public ResponseEntity<String> request(@AuthenticationPrincipal String from, @RequestBody String to){
        System.out.println("gf,fmsdfsd");
        relationService.request(from,to);
        return ResponseEntity.ok(from);
    }

    /*
        from : 로그인된 사용자가 to가 보낸 친구요청을 수락함
     */
    @PatchMapping("/relations")
    public ResponseEntity<Void> accept(@AuthenticationPrincipal String from, @RequestBody String to){
        relationService.accept(from,to);
        return ResponseEntity.ok().build();
    }

    /*
        from : 로그인된 사용자가 받았던 친구요청을 거절함
     */
    @DeleteMapping("/relations/{to}")
    public ResponseEntity<Void> reject(@AuthenticationPrincipal String from, @PathVariable String to){
        relationService.reject(from,to);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/relations")
    public ResponseEntity<List<Relation>> relationLists(@AuthenticationPrincipal String from){
        List<Relation> lists = relationService.relationLists(from);
        return ResponseEntity.ok(lists);
    }

    @GetMapping("/relations/{id}")
    public ResponseEntity<User> getUserInfo(@AuthenticationPrincipal String from, @PathVariable String id){
        User user = relationService.getUserInfo(id);
        return ResponseEntity.ok(user);
    }


}
