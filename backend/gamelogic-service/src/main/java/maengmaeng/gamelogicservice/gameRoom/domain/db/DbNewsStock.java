package maengmaeng.gamelogicservice.gameRoom.domain.db;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "news_stock")
public class DbNewsStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_stock_id")
    private Long newsStockId;

    @ManyToOne
    @JoinColumn(name = "news_id", referencedColumnName = "news_id")
    private DbNews dbNews;

    @ManyToOne
    @JoinColumn(name = "stock_id", referencedColumnName = "stock_id")
    private DbStock dbStock;

    @Column(name = "effect")
    private Integer effect;

}
