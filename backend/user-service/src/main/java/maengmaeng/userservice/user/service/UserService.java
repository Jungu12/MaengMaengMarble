package maengmaeng.userservice.user.service;

import lombok.RequiredArgsConstructor;
import maengmaeng.userservice.exception.ExceptionCode;
import maengmaeng.userservice.exception.UserException;
import maengmaeng.userservice.user.domain.Avatar;
import maengmaeng.userservice.user.domain.User;
import maengmaeng.userservice.user.domain.dto.UserDetail;
import maengmaeng.userservice.user.domain.dto.UserPossessionAvatar;
import maengmaeng.userservice.user.repository.AvatarRepository;
import maengmaeng.userservice.user.repository.UserAvatarRepository;
import maengmaeng.userservice.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserAvatarRepository userAvatarRepository;
    private final AvatarRepository avatarRepository;

    // 내 정보 조회(get)
    public UserDetail findUser(String userId) {
        // 유저 존재 확인
        User user = userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new UserException(ExceptionCode.USER_NOT_FOUND));
        // 내 정보를 DTO 형태로 보기 위해서 user의 값들과 avatar_id를 가져와서 만든다.
        UserDetail userDetail = UserDetail.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .point(user.getPoint())
                .win(user.getWin())
                .lose(user.getLose())
                .avatarId(user.getAvatar().getAvatarId()) // 엔티티에서 avatarID 필드 가져오기
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
                    userAvatar.setAvatarImage(avatar.getAvatarImage());
                    return userAvatar;
                })
                .collect(Collectors.toList());

        return userAvatars;
    }

    // 캐릭터 변경
    public void changeProfileAvatar(String userId, int newAvatarId) {
        // 유저 존재 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ExceptionCode.USER_NOT_FOUND));
        // 아바타 존재 확인
        Avatar newAvatar = avatarRepository.findById(newAvatarId)
                .orElseThrow(() -> new UserException(ExceptionCode.AVATAR_NOT_FOUND));

        // 유저의 적용된 아바타 변경
        user.changeAvatar(newAvatar);
        userRepository.save(user);
    }
}
