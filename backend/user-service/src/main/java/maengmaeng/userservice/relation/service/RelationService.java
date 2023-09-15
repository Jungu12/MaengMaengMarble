package maengmaeng.userservice.relation.service;

import lombok.RequiredArgsConstructor;
import maengmaeng.userservice.exception.ExceptionCode;
import maengmaeng.userservice.exception.RelationException;
import maengmaeng.userservice.exception.UserException;
import maengmaeng.userservice.user.domain.User;
import maengmaeng.userservice.user.repository.UserRepository;
import maengmaeng.userservice.relation.repository.RelationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import maengmaeng.userservice.relation.domain.Relation;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RelationService {

    private final RelationRepository relationRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addRelation(String loginUser, String to) {
        // 이미 친구 요청이 보내져있으면 아무 작업을 하지 않는다
        if (relationRepository.existsByFromIdAndToId(loginUser, to)) {
            throw new RelationException(ExceptionCode.ALREADY_REQUESTED);
        }
        // 없으면 친구신청 보냄
        Relation relation = Relation.builder()
                .fromId(loginUser)
                .toId(to)
                .build();

        relationRepository.save(relation);
    }


    @Transactional
    public void deleteRelation(String loginUser, String to){
        Relation relation = relationRepository.findByFromIdAndToId(loginUser,to).orElseThrow(()->new RelationException(ExceptionCode.FOLLOW_CANCEL_FAILED));
        int deleteCnt = relationRepository.deleteByFromIdAndToId(loginUser,to);
        if (deleteCnt == 0) {
            throw new RelationException(ExceptionCode.FOLLOW_CANCEL_FAILED);
        }
    }

    public List<Relation> relationLists(String from){
        List<Relation> relationList = relationRepository.findAllByFromId(from);
        return relationList;
    }

    public User getUserInfo(String id){
        User user = userRepository.findByUserId(id) .orElseThrow(() -> new UserException(ExceptionCode.USER_NOT_FOUND));
        return user;
    }


}
