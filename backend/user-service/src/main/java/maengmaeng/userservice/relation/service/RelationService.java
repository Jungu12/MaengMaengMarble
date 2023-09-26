package maengmaeng.userservice.relation.service;

import lombok.RequiredArgsConstructor;
import maengmaeng.userservice.exception.ExceptionCode;
import maengmaeng.userservice.exception.RelationException;
import maengmaeng.userservice.exception.UserException;
import maengmaeng.userservice.relation.domain.dto.RelationResponseDto;
import maengmaeng.userservice.relation.domain.dto.UserInfoResponseDto;
import maengmaeng.userservice.user.domain.Avatar;
import maengmaeng.userservice.user.domain.User;
import maengmaeng.userservice.user.domain.UserAvatar;
import maengmaeng.userservice.user.domain.dto.UserDetail;
import maengmaeng.userservice.user.repository.AvatarRepository;
import maengmaeng.userservice.user.repository.UserAvatarRepository;
import maengmaeng.userservice.user.repository.UserRepository;
import maengmaeng.userservice.relation.repository.RelationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import maengmaeng.userservice.relation.domain.Relation;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RelationService {

    private final RelationRepository relationRepository;
    private final UserRepository userRepository;
    private final UserAvatarRepository userAvatarRepository;
    private final AvatarRepository avatarRepository;
    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Transactional
    public void addRelation(String loginUser, String to) {
        // 이미 친구 요청이 보내져있으면 아무 작업을 하지 않는다
        if (relationRepository.existsByFromIdAndToId(loginUser, to)) {
            throw new RelationException(ExceptionCode.ALREADY_REQUESTED);
        }
        // 닉네임을 가진 사용작 없는 경우
        if(!userRepository.existsByNickname(to)){
            throw new RelationException(ExceptionCode.USER_NOT_FOUND);
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
        // 클라에서 친구id를 보내줌
        User toUser = userRepository.findUserByUserId(to).orElseThrow(()->new UserException(ExceptionCode.USER_NOT_FOUND));

        Relation relation = relationRepository.findByFromIdAndToId(loginUser,toUser.getNickname()).orElseThrow(()->new RelationException(ExceptionCode.FOLLOW_CANCEL_FAILED));
        int deleteCnt = relationRepository.deleteByFromIdAndToId(loginUser,toUser.getNickname());
        if (deleteCnt == 0) {
            throw new RelationException(ExceptionCode.FOLLOW_CANCEL_FAILED);
        }
    }

    public List<RelationResponseDto> relationLists(String from){
        // 내 친구목록들을 불러옴
        List<Relation> relationList = relationRepository.findAllByFromId(from);


        // 리스트가 0이면 아무도 없다는거니까 빈 리스트 바로 반환
        if(relationList.size()==0){
            return new ArrayList<>();
        }


        logger.info("relationList : {} " , relationList);
        logger.info("from (즉 현재 로그인된 사용자) : {} " , from);

        // 친구목록을 담을 lst
        List<RelationResponseDto> lst = new ArrayList<>();

        // 내가 팔로우한 친구 목록들을 불러오기
        for(Relation relation : relationList){ // 내 친구 목록을 돌면서

//            User user = userRepository.findByNickname(relation.getToId()).orElseThrow(()->new RelationException(ExceptionCode.USER_NOT_FOUND));
//
//            List<UserAvatar> userAvatars = user.getUserAvatars();
//            UserAvatar result = null;
//            for(UserAvatar avatar : userAvatars){
//                if(avatar.isMounting()){
//                    result = avatar;
//                }
//            }
//
//            Avatar resultAvatar = avatarRepository.findById(result.getAvatar().getAvatarId()).orElseThrow(()->new RelationException(ExceptionCode.AVATAR_NOT_FOUND));
//            String charaterImageUrl = resultAvatar.getAvatarImageBg();


            RelationResponseDto responseDto = RelationResponseDto.builder()
                    .userId(relation.getToId())
                    .nickname(relation.getToId())
//                    .character(charaterImageUrl)
                    .build();
            lst.add(responseDto);
        }
        return lst;
    }

    public UserInfoResponseDto getUserInfo(String id){
        User user = userRepository.findByUserId(id) .orElseThrow(() -> new UserException(ExceptionCode.USER_NOT_FOUND));

        List<UserAvatar> userAvatars = user.getUserAvatars();
        UserAvatar result = null;
        for(UserAvatar avatar : userAvatars){
            if(avatar.isMounting()){
                result = avatar;
            }
        }

        Avatar resultAvatar = avatarRepository.findById(result.getAvatar().getAvatarId()).orElseThrow(()->new RelationException(ExceptionCode.AVATAR_NOT_FOUND));
        String charaterImageUrl = resultAvatar.getAvatarImageBg();



        UserInfoResponseDto userInfoResponseDto = UserInfoResponseDto.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .point(user.getPoint())
                .win(user.getWin())
                .lose(user.getLose())
                .avatarImage(charaterImageUrl)
                .build();
        return userInfoResponseDto;
    }


}