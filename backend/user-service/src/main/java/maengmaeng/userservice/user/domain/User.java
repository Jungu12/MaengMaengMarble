package maengmaeng.userservice.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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
    private List<UserCharacter> userCharacters;

    @ManyToOne
    @JoinColumn(name = "character_id")
    private Character characterID; // 현재 프로필로 설정된 캐릭터

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
