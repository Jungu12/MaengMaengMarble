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

    @Column(nullable = false)
    private String nickname;

    private int point;

    private int win;

    private int lose;

    @OneToMany(mappedBy = "user")
    private List<UserAvatar> userAvatars;

    public User(String userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}