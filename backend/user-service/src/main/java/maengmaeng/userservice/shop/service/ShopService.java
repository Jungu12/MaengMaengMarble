package maengmaeng.userservice.shop.service;

import lombok.AllArgsConstructor;
import maengmaeng.userservice.exception.ExceptionCode;
import maengmaeng.userservice.exception.UserException;
import maengmaeng.userservice.shop.domain.dto.AvatarResponseDto;
import maengmaeng.userservice.shop.domain.dto.UserAvatarDto;
import maengmaeng.userservice.user.domain.Avatar;

import maengmaeng.userservice.user.domain.User;
import maengmaeng.userservice.user.domain.UserAvatar;
import maengmaeng.userservice.user.repository.AvatarRepository;
import maengmaeng.userservice.user.repository.UserAvatarRepository;
import maengmaeng.userservice.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ShopService {

    private final AvatarRepository avatarRepository;
    private final UserRepository userRepository;
    private final UserAvatarRepository userAvatarRepository;


    /*
    나중에 내가 가지고있는 캐릭터 리스트를 반환해야할 듯
     */
    public List<AvatarResponseDto> getAvatars(String loginUser){
        List<Avatar> avatars = avatarRepository.findAll();
        List<AvatarResponseDto> avatarResponseDtoList = new ArrayList<>();

        return avatars.stream()
                .map(avatar -> AvatarResponseDto.builder()
                        .avatarId(avatar.getAvatarId())
                        .avatarImage(avatar.getAvatarImage())
                        .avatarName(avatar.getAvatarName())
                        .avatarPrice(avatar.getAvatarPrice())
                        .build())
                .collect(Collectors.toList());

    }

    @Transactional
    public void buyAvatar(String loginUser, int id){
        // 잔여포인트 확인
        User user = userRepository.findByUserId(loginUser).orElseThrow(() -> new UserException(ExceptionCode.USER_NOT_FOUND));
        int point = user.getPoint();
        Avatar avatar = avatarRepository.findById(id).orElseThrow(()-> new UserException(ExceptionCode.AVATAR_NOT_FOUND));
        
        // 포인트 부족 시
        if(point < Integer.parseInt(avatar.getAvatarPrice())){
            new UserException(ExceptionCode.POINT_NOT_SUFFICIENT);
        }

        // 이미 보유한 캐릭터를 사려고하는 경우
        boolean haved = userAvatarRepository.existsByUserUserIdAndAvatarAvatarId(user.getUserId(), avatar.getAvatarId());
        if(haved){
            throw new UserException(ExceptionCode.AVATAR_EXISTED);
        }

        UserAvatar userAvatar = UserAvatar.builder()
                .avatar(avatar)
                .user(user)
                .build();
        userAvatarRepository.save(userAvatar);
    }
}
