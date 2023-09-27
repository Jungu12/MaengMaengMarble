package maengmaeng.gamelogicservice.gameRoom.domain.db;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "news")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class DbNews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    private Long newsId;

    @Column(name = "news_type")
    private String newsType;

    @Column(name = "news_title")
    private String newsTitle;

    @Column(name = "news_image")
    private String newsImage;

    @Column(name = "news_content", length = 150)
    private String newsContent;


    @OneToMany(mappedBy = "dbNews", fetch = FetchType.LAZY)
    private List<DbNewsCountry> dbNewsCountryList;

    @OneToMany(mappedBy = "dbNews", fetch = FetchType.LAZY)
    private List<DbNewsStock> dbNewsStockList;
}
