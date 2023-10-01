package maengmaeng.gamelogicservice.gameRoom.domain.db;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "country")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class DbCountry {

    @Id
    @Column(name = "country_id")
    private Integer countryId;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "land_price")
    private Integer landPrice;

    @Column(name = "villa_price")
    private Integer villaPrice;

    @Column(name = "building_price")
    private Integer buildingPrice;

    @Column(name = "hotel_price")
    private Integer hotelPrice;

    @Column(name = "land_fee")
    private Integer landFee;

    @Column(name = "villa_fee")
    private Integer villaFee;

    @Column(name = "hotel_fee")
    private Integer hotelFee;

    @Column(name = "building_fee")
    private Integer buildingFee;



}
