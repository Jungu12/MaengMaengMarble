package maengmaeng.userservice.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserPossessionAvatar {
    private int avatarId;
    private String avatarName;
    private boolean hasAvatar;
    private String avatarPrice;
    private String avatarImage;

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }

    public void setHasAvatar(boolean contains) {
        this.hasAvatar = contains;
    }

    public void setAvatarPrice(String avatarPrice) {
        this.avatarPrice = avatarPrice;
    }

    public void setAvatarImage(String avatarImage) {
        this.avatarImage = avatarImage;
    }

    public UserPossessionAvatar() {
    }
}
