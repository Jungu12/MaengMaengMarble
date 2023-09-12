package maengmaeng.userservice.relation.service;

import lombok.RequiredArgsConstructor;
import maengmaeng.userservice.exception.ExceptionCode;
import maengmaeng.userservice.exception.RelationException;
import maengmaeng.userservice.exception.UserException;
import maengmaeng.userservice.myinfo.domain.User;
import maengmaeng.userservice.myinfo.repository.UserRepository;
import maengmaeng.userservice.relation.domain.dto.RelationUserInfo;
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
    public void request(String from, String to) {
        // 이미 친구 요청이 보내져있거나(false) 친구이면(true) 아무 작업을 하지 않는다
        if (relationRepository.existsByFromIdAndToId(from, to)) {
            throw new RelationException(ExceptionCode.ALREADY_REQUESTED);
        }
        // 없으면 친구신청 보냄
        Relation relation = Relation.builder()
                .fromId(from)
                .toId(to)
                .status(false)
                .build();

        relationRepository.save(relation);
    }

    // 친구 요청 수락
    @Transactional
    public void accept(String from, String to) {
        Relation relation = relationRepository.findByFromIdAndToId(from, to).orElseThrow(()->new RelationException((ExceptionCode.FOLLOW_CANCEL_FAILED)));
        relation.setStatus(true);
        relationRepository.save(relation);
    }

    @Transactional
    public void reject(String from, String to){
        Relation relation = relationRepository.findByFromIdAndToId(from, to).orElseThrow(()->new RelationException(ExceptionCode.ALREADY_REQUESTED));
        int deleteCnt = relationRepository.deleteByFromIdAndToId(from,to);
        if (deleteCnt == 0) {
            throw new RelationException(ExceptionCode.FOLLOW_CANCEL_FAILED);
        }
    }

    public List<Relation> relationLists(String from){
        List<Relation> relationList = relationRepository.findAllByFromId(from);
        return relationList;
    }

    public User getUserInfo(String id){
        User user = userRepository.findUserByUserId(id) .orElseThrow(() -> new UserException(ExceptionCode.USER_NOT_FOUND));
        return user;
    }


}
