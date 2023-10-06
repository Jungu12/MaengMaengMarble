package maengmaeng.gamelogicservice.gameRoom.domain.db;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "card")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class DbCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cardId;
    private String cardName;
    private String cardImage;


}
