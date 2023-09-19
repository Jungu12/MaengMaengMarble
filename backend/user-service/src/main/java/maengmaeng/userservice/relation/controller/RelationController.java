package maengmaeng.userservice.relation.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maengmaeng.userservice.relation.domain.dto.RelationResponseDto;
import maengmaeng.userservice.user.domain.User;
import maengmaeng.userservice.relation.domain.Relation;
import maengmaeng.userservice.relation.service.RelationService;
import maengmaeng.userservice.user.domain.dto.UserDetail;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user-service/relation")
public class RelationController {

    private final RelationService relationService;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    /*
        from : 로그인된 사용자, 친구 요청 보내는 사람
        to : 내가 친구요청 하는 사람
     */
    @PostMapping("/relations")
    public ResponseEntity<String> addRelation(@AuthenticationPrincipal String loginUser, @RequestBody Map<String, String> to){
        relationService.addRelation(loginUser,to.get("to"));
        return ResponseEntity.ok(loginUser);
    }


    @DeleteMapping("/relations/{to}")
    public ResponseEntity<Void> deleteRelation(@AuthenticationPrincipal String loginUser, @PathVariable String to){
        relationService.deleteRelation(loginUser,to);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/relations")
    public ResponseEntity<List<RelationResponseDto>> relationLists(@AuthenticationPrincipal String loginUser){
        List<RelationResponseDto> lists = relationService.relationLists(loginUser);
        return ResponseEntity.ok(lists);
    }

    @GetMapping("/relations/detail/{id}")
    public ResponseEntity<UserDetail> getUserInfo(@PathVariable String id){
        UserDetail user = relationService.getUserInfo(id);
        return ResponseEntity.ok(user);
    }


}