package maengmaeng.userservice.relation.service;

import maengmaeng.userservice.exception.RelationException;
import maengmaeng.userservice.relation.domain.Relation;
import maengmaeng.userservice.relation.domain.dto.RelationResponseDto;
import maengmaeng.userservice.relation.domain.dto.UserInfoResponseDto;
import maengmaeng.userservice.relation.repository.RelationRepository;
import maengmaeng.userservice.relation.service.RelationService;
import maengmaeng.userservice.user.domain.Avatar;
import maengmaeng.userservice.user.domain.User;
import maengmaeng.userservice.user.domain.UserAvatar;
import maengmaeng.userservice.user.domain.dto.UserDetail;
import maengmaeng.userservice.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class RelationServiceTest {

    @InjectMocks
    private RelationService relationService;
    @Mock
    private RelationRepository relationRepository;
    @Mock
    private UserRepository userRepository;

    private String from;
    private String to;
    private Relation relation;
    private User user;
    private UserAvatar userAvatar;

    private Avatar avatar;

    @BeforeEach
    void setUp() {
        from = "user1@naver.com";
        to = "user2@naver.com";
        relation = Relation.builder()
                .relationId(1L)
                .fromId("user1@naver.com")
                .toId("user2@naver.com")
                .build();
        avatar = new Avatar(1, "아바타", "이미지", "30000", null);
        user = new User("user@naver.com", "유저1");
        userAvatar = new UserAvatar(user,avatar,false);


    }


    @Test
    @DisplayName("친구 추가")
    void addRelation_success() {
        // given
        given(relationRepository.existsByFromIdAndToId(from, to)).willReturn(false);

        relationService.addRelation(from, to);
    }

    @Test
    @DisplayName("친구 추가 실패")
    void addRelation_fail() {
        // given
        given(relationRepository.existsByFromIdAndToId(from, to)).willReturn(true);

        // when & then
        assertThrows(RelationException.class, () -> relationService.addRelation(from, to));
    }

    @Test
    @DisplayName("친구 삭제 성공")
    void deleteRelation_success() {
        // given
        given(relationRepository.findByFromIdAndToId(from, to)).willReturn(Optional.ofNullable(relation));
        given(relationRepository.deleteByFromIdAndToId(from, to)).willReturn(1);

        relationService.deleteRelation(from, to);
    }

    @Test
    @DisplayName("친구 삭제 실패 - 신청된 친구가 없을 때")
    void deleteRelation_fail() {
        // given
        given(relationRepository.findByFromIdAndToId(from, to)).willReturn(Optional.empty());

        // when & then
        assertThrows(RelationException.class, () -> relationService.deleteRelation(from, to));
    }

    @Test
    @DisplayName("친구 삭제 실패 - 삭제에 실패했을 때")
    void deleteRelation_delete_fail() {
        // given
        given(relationRepository.findByFromIdAndToId(from, to)).willReturn(Optional.ofNullable(relation));
        given(relationRepository.deleteByFromIdAndToId(from, to)).willReturn(0);

        // when & then
        assertThrows(RelationException.class, () -> relationService.deleteRelation(from, to));
    }


    @Test
    @DisplayName("친구 목록 성공")
    void relationLists_success() {
        // given
        List<Relation> relationList = List.of(relation);
        given(relationRepository.findAllByFromId(from)).willReturn(relationList);

        // when
        List<RelationResponseDto> result = relationService.relationLists(from);

        // then

    }

    @Test
    @DisplayName("회원 상세정보 조회")
    void userInfo_success() {
        //given
        given(userRepository.findByUserId("user@naver.com")).willReturn(Optional.ofNullable(user));


        Avatar compAvatar = new Avatar(1, "아바타", "이미지", "30000", null);


        // when
        UserInfoResponseDto user = relationService.getUserInfo("user@naver.com");

        // then
        assertThat(user.getUserId()).isEqualTo("user@naver.com");
        assertThat(user.getNickname()).isEqualTo("유저1");

    }
}