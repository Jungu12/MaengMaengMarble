package maengmaeng.userservice.shop.service;

import lombok.AllArgsConstructor;
import maengmaeng.userservice.exception.ExceptionCode;
import maengmaeng.userservice.exception.UserException;
import maengmaeng.userservice.shop.domain.dto.AvatarResponseDto;
import maengmaeng.userservice.shop.domain.dto.ShopResponseDto;
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



    public ShopResponseDto getAvatars(String loginUser){
        List<Avatar> avatars = avatarRepository.findAll();
        List<AvatarResponseDto> avatarResponseDtoList = new ArrayList<>();

        List<Integer> userAvatarIds = userAvatarRepository.findUserAvatarsByUserId(loginUser);

        for(Avatar avatar : avatars){
            AvatarResponseDto avatarDto = AvatarResponseDto
                    .builder()
                    .avatarId(avatar.getAvatarId())
                    .avatarPrice(Integer.parseInt(avatar.getAvatarPrice()))
                    .avatarName(avatar.getAvatarName())
                    .avatarImage(avatar.getAvatarImage())
                    .hasAvatar(userAvatarIds.contains(avatar.getAvatarId()))
                    .build();

            avatarResponseDtoList.add(avatarDto);
        }

        User user = userRepository.findByUserId(loginUser).orElseThrow(() -> new UserException(ExceptionCode.USER_NOT_FOUND));
        int point = user.getPoint();

        ShopResponseDto result = ShopResponseDto.builder()
                .avatarList(avatarResponseDtoList)
                .point(point)
                .build();

        return result;

    }

    @Transactional
    public void buyAvatar(String loginUser, int id){
        // 잔여포인트 확인
        User user = userRepository.findByUserId(loginUser).orElseThrow(() -> new UserException(ExceptionCode.USER_NOT_FOUND));
        int point = user.getPoint();
        Avatar avatar = avatarRepository.findById(id).orElseThrow(()-> new UserException(ExceptionCode.AVATAR_NOT_FOUND));
        
        // 포인트 부족 시
        if(point < Integer.parseInt(avatar.getAvatarPrice())){
            throw new UserException(ExceptionCode.POINT_NOT_SUFFICIENT);
        }

        // 이미 보유한 캐릭터를 사려고하는 경우
        boolean haved = userAvatarRepository.existsByUserUserIdAndAvatarAvatarId(user.getUserId(), avatar.getAvatarId());
        if(haved){
            throw new UserException(ExceptionCode.AVATAR_EXISTED);
        }

        UserAvatar userAvatar = new UserAvatar(user,avatar,false);
        user.setPointSub(Integer.parseInt(avatar.getAvatarPrice()));
        userAvatarRepository.save(userAvatar);
    }
}
