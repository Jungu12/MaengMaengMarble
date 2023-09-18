package maengmaeng.userservice.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAvatar {

    @Id
    @Column(name = "user_avatar_id")
    private int userAvatarId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "avatar_id")
    private Avatar avatar;

    private boolean mounting;

    public UserAvatar(User user, Avatar avatar, boolean mounting) {
        this.user = user;
        this.avatar = avatar;
        this.mounting = mounting;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public void setMounting(boolean mounting) {
        this.mounting = mounting;
    }
}
