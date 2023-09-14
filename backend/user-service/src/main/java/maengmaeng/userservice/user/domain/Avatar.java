package maengmaeng.userservice.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Objects;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

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

    @Column(name = "avatar_image")
    private String avatarImage;

    @Column(name = "avatar_price")
    private String avatarPrice;

    @OneToMany(mappedBy = "avatar")
    private List<UserAvatar> userAvatars;

    public void setUserAvatars(List<UserAvatar> userAvatars){
        this.userAvatars = userAvatars;
    }



}
