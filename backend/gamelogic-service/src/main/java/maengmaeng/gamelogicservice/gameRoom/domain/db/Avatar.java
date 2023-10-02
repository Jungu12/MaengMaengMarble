package maengmaeng.gamelogicservice.gameRoom.domain.db;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "avatar")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int avatarId;
    private String avatarName;
    private String avatarImageBg;
    private String avatarImageNoBg;


}
