package maengmaeng.gamelogicservice.gameRoom.domain.db;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import maengmaeng.gamelogicservice.gameRoom.domain.News;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "news_country")
public class DbNewsCountry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_country_id")
    private Long newsCountryId;

    @ManyToOne
    @JoinColumn(name = "news_id", referencedColumnName = "news_id")
    private DbNews dbNews;

    @ManyToOne
    @JoinColumn(name = "country_id", referencedColumnName = "country_id")
    private DbCountry dbCountry;

    @Column(name = "effect")
    private Integer effect;

}
