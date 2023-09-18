package maengmaeng.userservice.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    private String nickname;

    private int point;

    private int win;

    private int lose;

    @OneToMany(mappedBy = "user")
    private List<UserAvatar> userAvatars;

    @ManyToOne
    @JoinColumn(name = "avatar_id")
    private Avatar avatar; // 현재 프로필로 설정된 캐릭터

    public User(String userId) {
        this.userId = userId;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeAvatar(Avatar newProfileAvatar) {
        this.avatar = newProfileAvatar;
    }

    public void setUserAvatarsForTest(List<UserAvatar> userAvatars){
        this.userAvatars = userAvatars;
    }

    public void setAvatarForTest(Avatar avatar){
        this.avatar = avatar;
    }
}
