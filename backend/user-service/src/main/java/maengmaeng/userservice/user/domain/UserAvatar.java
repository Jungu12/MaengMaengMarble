package maengmaeng.userservice.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import maengmaeng.userservice.user.repository.AvatarRepository;

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

    public void setAvatarForTest(Avatar avatar){
        this.avatar =avatar;
    }

    @Builder
    public UserAvatar(User user, Avatar avatar){
        this.user = user;
        this. avatar = avatar;
    }
}
