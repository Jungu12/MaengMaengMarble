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
public class Character {

    @Id
    @Column(name = "character_id", nullable = false)
    private int characterId;

    @Column(name = "character_name")
    private String characterName;

    @Column(name = "character_image")
    private String characterImage;

    @Column(name = "character_price")
    private String characterPrice;

    @OneToMany(mappedBy = "character")
    private List<UserCharacter> userCharacters;
}
