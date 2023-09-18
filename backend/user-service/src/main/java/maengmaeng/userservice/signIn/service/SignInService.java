package maengmaeng.userservice.signIn.service;

import maengmaeng.userservice.exception.ExceptionCode;
import maengmaeng.userservice.exception.UserException;
import maengmaeng.userservice.user.domain.User;
import maengmaeng.userservice.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignInService {
    private static UserRepository userRepository;
    @Transactional
   public void changeNickname(String loginUser, String nickname){
        User user = userRepository.findUserByUserId(loginUser).orElseThrow(()->new UserException(ExceptionCode.USER_NOT_FOUND));
        user.setNickname(nickname);
        userRepository.save(user);
   }
}
