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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;

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
    @PostMapping("/api/relations/{loginUser}")
    public ResponseEntity<String> addRelation(@PathVariable String loginUser, @RequestBody Map<String, String> to){
        relationService.addRelation(loginUser,to.get("to"));
        return ResponseEntity.ok(loginUser);
    }

    /*
        from : 로그인된 사용자가 받았던 친구요청을 거절함
     */
    @DeleteMapping("/api/relations/{to}/{loginUser}")
    public ResponseEntity<Void> deleteRelation(@PathVariable String loginUser, @PathVariable String to){
        relationService.deleteRelation(loginUser,to);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/relations/{loginUser}")
    public ResponseEntity<List<RelationResponseDto>> relationLists(@PathVariable String loginUser){
        List<RelationResponseDto> lists = relationService.relationLists(loginUser);
        return ResponseEntity.ok(lists);
    }

    @GetMapping("/api/relations/detail/{id}")
    public ResponseEntity<UserDetail> getUserInfo(@PathVariable String id){
        UserDetail user = relationService.getUserInfo(id);
        return ResponseEntity.ok(user);
    }


}
