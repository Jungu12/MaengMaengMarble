package maengmaeng.userservice.user.service;

import lombok.RequiredArgsConstructor;
import maengmaeng.userservice.exception.ExceptionCode;
import maengmaeng.userservice.exception.UserException;
import maengmaeng.userservice.user.domain.Avatar;
import maengmaeng.userservice.user.domain.User;
import maengmaeng.userservice.user.domain.dto.UserDetail;
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
        User user = userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new UserException(ExceptionCode.USER_NOT_FOUND));

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
        // 중복 체크 로직
        return userRepository.existsByNickname(nickname);
    }

    // 닉네임 변경
    public void changeNickname(String userId, String newNickname) {
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
    public List<Avatar> getMyAvatars(String userId) {
        List<Integer> userAvatarIds = userAvatarRepository.findUserAvatarsByUserId(userId);

        List<Avatar> allAvatars = avatarRepository.findAll();

        List<Avatar> userAvatars = allAvatars.stream()
                .filter(avatar -> userAvatarIds.contains(avatar.getAvatarId()))
                .collect(Collectors.toList());

        return userAvatars;
    }

    // 캐릭터 변경
    public void changeProfileAvatar(String userId, int newAvatarId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ExceptionCode.USER_NOT_FOUND));

        Avatar newAvatar = avatarRepository.findById(newAvatarId)
                .orElseThrow(() -> new UserException(ExceptionCode.AVATAR_NOT_FOUND));

        user.changeAvatar(newAvatar);

        userRepository.save(user);
    }
}
