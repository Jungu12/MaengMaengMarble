package maengmaeng.userservice.service;

import maengmaeng.userservice.exception.RelationException;
import maengmaeng.userservice.relation.domain.Relation;
import maengmaeng.userservice.relation.repository.RelationRepository;
import maengmaeng.userservice.relation.service.RelationService;
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

    private String from;
    private String to;
    private Relation relation;

    @BeforeEach
    void setUp(){
       from = "user1@naver.com";
       to = "user2@naver.com";
       relation = Relation.builder()
               .relationId(1L)
                .fromId("user1@naver.com")
                .toId("user2@naver.com")
                .build();
    }


    @Test
    @DisplayName("친구 추가")
    void addRelation_success(){
        // given
        given(relationRepository.existsByFromIdAndToId(from,to)).willReturn(false);

        relationService.addRelation(from,to);
    }

    @Test
    @DisplayName("친구 추가 실패")
    void addRelation_fail(){
        // given
        given(relationRepository.existsByFromIdAndToId(from, to)).willReturn(true);

        // when & then
        assertThrows(RelationException.class, ()->relationService.addRelation(from,to));
    }

    @Test
    @DisplayName("친구 삭제 성공")
    void deleteRelation_success(){
        // given
        given(relationRepository.findByFromIdAndToId(from,to)).willReturn(Optional.ofNullable(relation));
        given(relationRepository.deleteByFromIdAndToId(from,to)).willReturn(1);

        relationService.deleteRelation(from, to);
    }

    @Test
    @DisplayName("친구 삭제 실패 - 신청된 친구가 없을 때")
    void deleteRelation_fail(){
        // given
        given(relationRepository.findByFromIdAndToId(from,to)).willReturn(Optional.empty());

        // when & then
        assertThrows(RelationException.class, ()->relationService.deleteRelation(from,to));
    }

    @Test
    @DisplayName("친구 삭제 실패 - 삭제에 실패했을 때")
    void deleteRelation_delete_fail(){
        // given
        given(relationRepository.findByFromIdAndToId(from,to)).willReturn(Optional.ofNullable(relation));
        given(relationRepository.deleteByFromIdAndToId(from,to)).willReturn(0);

        // when & then
        assertThrows(RelationException.class, ()->relationService.deleteRelation(from,to));
    }


    @Test
    @DisplayName("친구 목록 성공")
    void relationLists_success(){
        // given
        List<Relation> relationList = List.of(relation);
        given(relationRepository.findAllByFromId(from)).willReturn(relationList);

        // when
        List<Relation> result = relationService.relationLists(from);

        // then
        assertThat(result.get(0).getRelationId()).isEqualTo(1L);
        assertThat(result.get(0).getFromId()).isEqualTo("user1@naver.com");
        assertThat(result.get(0).getToId()).isEqualTo("user2@naver.com");
    }
}
