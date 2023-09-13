package maengmaeng.userservice.user.service;

import lombok.RequiredArgsConstructor;
import maengmaeng.userservice.exception.ExceptionCode;
import maengmaeng.userservice.exception.UserException;
import maengmaeng.userservice.user.domain.Character;
import maengmaeng.userservice.user.domain.User;
import maengmaeng.userservice.user.domain.UserCharacter;
import maengmaeng.userservice.user.repository.CharacterRepository;
import maengmaeng.userservice.user.repository.UserCharacterRepository;
import maengmaeng.userservice.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserCharacterRepository userCharacterRepository;
    private final CharacterRepository characterRepository;

    // 내 정보 조회(get)
    public User findUser(String userId) {
        User user = userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new UserException(ExceptionCode.USER_NOT_FOUND));
        return user;
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
    public List<Character> getMyCharacters(String userId) {
        List<Integer> userCharacterIds = userCharacterRepository.findUserCharactersByUserId(userId);

        List<Character> allCharacters = characterRepository.findAll();

        List<Character> userCharacters = allCharacters.stream()
                .filter(character -> userCharacterIds.contains(character.getCharacterId()))
                .collect(Collectors.toList());

        return userCharacters;
    }
}
