package maengmaeng.userservice.user.service;

import lombok.RequiredArgsConstructor;
import maengmaeng.userservice.exception.ExceptionCode;
import maengmaeng.userservice.exception.UserException;
import maengmaeng.userservice.relation.domain.Relation;
import maengmaeng.userservice.relation.repository.RelationRepository;
import maengmaeng.userservice.user.domain.Avatar;
import maengmaeng.userservice.user.domain.User;
import maengmaeng.userservice.user.domain.UserAvatar;
import maengmaeng.userservice.user.domain.dto.UserDetail;
import maengmaeng.userservice.user.domain.dto.UserPossessionAvatar;
import maengmaeng.userservice.user.repository.AvatarRepository;
import maengmaeng.userservice.user.repository.UserAvatarRepository;
import maengmaeng.userservice.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserAvatarRepository userAvatarRepository;
    private final AvatarRepository avatarRepository;
    private final RelationRepository relationRepository;

    // 내 정보 조회(get)
    public UserDetail findUser(String userId) {
        // 유저 존재 확인
        User user = userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new UserException(ExceptionCode.USER_NOT_FOUND));
        // "avatarId" 가져오기
        Avatar avatarIdOptional = userRepository.findMountedAvatarIdByUserId(userId);

        int avatarId = avatarIdOptional.getAvatarId(); // 만약 마운트된 아바타가 없으면 기본값으로 0을 사용하거나 다른 방법으로 처리할 수 있습니다.
        String avatarImageBg = avatarIdOptional.getAvatarImageBg();
        String avatarImageNoBg = avatarIdOptional.getAvatarImageNoBg();
        // 내 정보를 DTO 형태로 보기 위해서 user의 값들과 avatar_id를 가져와서 만든다.
        UserDetail userDetail = UserDetail.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .point(user.getPoint())
                .win(user.getWin())
                .lose(user.getLose())
                .avatarId(avatarId)
                .avatarImageBg(avatarImageBg)
                .avatarImageNoBg(avatarImageNoBg)
                .build();

        return userDetail;
    }

    // 닉네임 중복 확인(get)
    public boolean isNicknameDuplicated(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    // 닉네임 변경
    public void changeNickname(String userId, String newNickname) {
        // 유저 존재 확인
        User user = userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new UserException(ExceptionCode.USER_NOT_FOUND));

        // 새 닉네임이 이미 사용중인지 확인
        if (userRepository.existsByNickname(newNickname)) {
            throw new UserException(ExceptionCode.NICKNAME_ALREADY_IN_USE);
        }

        List<Relation> relationList = relationRepository.findAllByToId(user.getNickname());
        System.out.println(relationList.size());
        for(Relation relation : relationList){
            relation.setToId(newNickname);
            relationRepository.save(relation);
        }

        // 닉네임 변경
        user.setNickname(newNickname);
        userRepository.save(user);
    }

    // 내가 보유한 캐릭터 조회
    public List<UserPossessionAvatar> getMyAvatars(String userId) {
        // 유저가 보유하고 있는 아바타들을 리스트로 만든다.
        List<Integer> userAvatarIds = userAvatarRepository.findUserAvatarsByUserId(userId);
        // 모든 아바타들을 리스트로 만든다.
        List<Avatar> allAvatars = avatarRepository.findAll();

        // 유저가 보유한 아바타들을 표현하기 위한 리스트를 만든다.
        List<UserPossessionAvatar> userAvatars = allAvatars.stream()
                .map(avatar -> {
                    UserPossessionAvatar userAvatar = new UserPossessionAvatar();
                    userAvatar.setAvatarId(avatar.getAvatarId());
                    userAvatar.setAvatarName(avatar.getAvatarName());
                    userAvatar.setHasAvatar(userAvatarIds.contains(avatar.getAvatarId()));
                    userAvatar.setAvatarPrice(avatar.getAvatarPrice());
                    userAvatar.setAvatarImageBg(avatar.getAvatarImageBg());
                    userAvatar.setAvatarImageNoBg(avatar.getAvatarImageNoBg());
                    return userAvatar;
                })
                .collect(Collectors.toList());

        return userAvatars;
    }


    // 캐릭터 변경
    @Transactional
    public void changeProfileAvatar(String userId, int newAvatarId) {
        // 현재 마운트된 아바타 중에서 `mounting` 컬럼을 `true`에서 `false`로 변경
        userAvatarRepository.unmountAvatarByUserId(userId);

        // 선택한 새로운 아바타의 `mounting` 컬럼을 `false`에서 `true`로 변경
        userAvatarRepository.mountAvatarByUserIdAndAvatarId(userId, newAvatarId);
    }
}
