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
    private int avatarPrice;
    private String avatarImageBg;
    private String avatarImageNoBg;

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }

    public void setHasAvatar(boolean contains) {
        this.hasAvatar = contains;
    }

    public void setAvatarPrice(int avatarPrice) {
        this.avatarPrice = avatarPrice;
    }

    public void setAvatarImageBg(String avatarImageBg) {
        this.avatarImageBg = avatarImageBg;
    }
    public void setAvatarImageNoBg(String avatarImageNoBg) {
        this.avatarImageNoBg = avatarImageNoBg;
    }

    public UserPossessionAvatar() {
    }
}
