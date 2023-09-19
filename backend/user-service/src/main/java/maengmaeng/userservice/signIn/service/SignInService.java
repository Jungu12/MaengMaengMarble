package maengmaeng.userservice.signIn.service;

import lombok.RequiredArgsConstructor;
import maengmaeng.userservice.exception.ExceptionCode;
import maengmaeng.userservice.exception.UserException;
import maengmaeng.userservice.user.domain.User;
import maengmaeng.userservice.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignInService {
    private final UserRepository userRepository;
    @Transactional
   public void changeNickname(String loginUser, String nickname){
        User user = userRepository.findByUserId(loginUser).orElseThrow(()->new UserException(ExceptionCode.USER_NOT_FOUND));
        user.setNickname(nickname);
        userRepository.save(user);
   }
}
