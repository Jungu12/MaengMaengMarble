package maengmaeng.gamelogicservice.gameRoom.domain.db;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "stock")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class DbStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private int stockId;

    @Column(name = "stock_name")
    private String stockName;

    @Column(name = "stock_price")
    private int stockPrice;

    @Column(name = "industry_id")
    private int industryId;

    @Column(name = "dividends")
    private Double dividends;


}
