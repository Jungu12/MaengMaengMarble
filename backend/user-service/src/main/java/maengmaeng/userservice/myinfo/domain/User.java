package maengmaeng.userservice.myinfo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
