package maengmaeng.userservice.user.domain;

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
public class Avatar {

    @Id
    @Column(name = "avatar_id", nullable = false)
    private int avatarId;

    @Column(name = "avatar_name")
    private String avatarName;

    @Column(name = "avatar_image_bg")
    private String avatarImageBg;

    @Column(name = "avatar_image_no_bg")
    private String avatarImageNoBg;

    @Column(name = "avatar_price")
    private int avatarPrice;

    @OneToMany(mappedBy = "avatar")
    private List<UserAvatar> userAvatars;

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }



}
