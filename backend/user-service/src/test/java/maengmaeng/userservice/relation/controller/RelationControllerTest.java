package maengmaeng.userservice.relation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import maengmaeng.userservice.relation.domain.Relation;
import maengmaeng.userservice.relation.service.RelationService;
import maengmaeng.userservice.user.domain.Avatar;
import maengmaeng.userservice.user.domain.User;
import maengmaeng.userservice.user.domain.UserAvatar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(RelationController.class)
@EnableWebMvc
public class RelationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RelationService relationService;

    @Test
    @DisplayName("친구 추가하기")
    void request() throws Exception {
        // given
        Map<String, String> to = new HashMap<>();
        to.put("to", "user2@naver.com");
        String loginUser = "user1@naver.com";

        // when & then
        mockMvc.perform(post("/user-service/relation/api/relations/{loginUser}", loginUser)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(to)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("user1@naver.com"));
    }



    @Test
    @DisplayName("친구 삭제")
    void reject() throws Exception {
        // given
        String loginUser = "user1@naver.com";
        String to = "user2@naver.com";

        // when & then
        mockMvc.perform(delete("/user-service/relation/api/relations/{to}/{loginUser}", to, loginUser)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("친구 리스트")
    void relationLists() throws Exception {
        // given
        Relation relation = Relation.builder()
                .relationId(1L)
                .fromId("user1@naver.com")
                .toId("user2@naver.com")
                .build();
        String loginUser = "user1@naver.com";
        given(relationService.relationLists(loginUser)).willReturn(List.of(relation));

        // when & then
        mockMvc.perform(get("/user-service/relation/api/relations/{loginUser}", loginUser)
                .contentType("application/json"))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("회원 상세정보 조회")
    void userInfo() throws Exception {
        // given
        Avatar avatar = new Avatar(1,"아바타","이미지","30000",null);
        User user = new User("user@naver.com","유저1",50000,3,1,null,null);
        UserAvatar userAvatar = new UserAvatar(1,user,avatar);

        avatar.setUserAvatars(List.of(userAvatar));
        user.setUserAvatarsForTest(List.of(userAvatar));
        user.setAvatarForTest(avatar);

        given(relationService.getUserInfo("user@naver.com")).willReturn(user);


        // when & then
        mockMvc.perform(get("/user-service/relation/api/relations/detail/{id}", user)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }
}
