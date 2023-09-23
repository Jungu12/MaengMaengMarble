package maengmaeng.gamelogicservice.gameRoom.domain;

import maengmaeng.gamelogicservice.gameRoom.domain.db.DbCountry;
import maengmaeng.gamelogicservice.gameRoom.domain.db.DbNews;
import org.springframework.stereotype.Component;

@Component
public class GameInfoMapper {


    /** DB 데이터 REDIS 에 저장할 DATA로 전환
     * */
    public Land toRedisLand(DbCountry dbCountry){
        int[] buildingPrices = { dbCountry.getVillaPrice(), dbCountry.getBuildingPrice(),dbCountry.getHotelPrice()};
        int[] fees = {dbCountry.getVillaFee(),dbCountry.getBuildingFee(), dbCountry.getHotelPrice()};
        boolean [] buildings = {false,false,false,false};

        return Land.builder()
                .landId(dbCountry.getCountryId())
                .name(dbCountry.getCountryName())
                .landPrice(dbCountry.getLandPrice())
                .buildingPrices(buildingPrices)
                .fees(fees)
                .currentLandPrice(dbCountry.getLandPrice())
                .buildings(buildings)
                .currentFees(fees)
                .owner(-1)
                .build();

    }

    /***/
    public News toRedisNews(DbNews)
}
