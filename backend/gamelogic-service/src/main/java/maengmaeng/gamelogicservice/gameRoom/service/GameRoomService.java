package maengmaeng.gamelogicservice.gameRoom.service;

import lombok.AllArgsConstructor;
import maengmaeng.gamelogicservice.gameRoom.domain.db.*;
import maengmaeng.gamelogicservice.gameRoom.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GameRoomService {

    private final DbCountryRespository dbCountryRespository;
    private final DbNewsRepository dbNewsRepository;
    private final DbStockRepository dbStockRepository;
    private final DbNewsCountryRepository dbNewsCountryRepository;
    private final DbCardRepository dbCardRepository;
    private final DbNewsStockRepository dbNewsStockRepository;
    private final GameInfoRepository gameInfoRepository;
    /** 나라 목록 가져오기
     * */
    public void getInfo (){
        List<DbCountry> countries = dbCountryRespository.findAll();
        for(DbCountry dbCountry: countries){
            System.out.println(dbCountry.toString());
        }
        List<DbNews> news = dbNewsRepository.findAll();
        for(DbNews dbNews: news) {
            System.out.println(dbNews.toString());
        }
        List<DbStock> stocks = dbStockRepository.findAll();
        for(DbStock stock: stocks ){
            System.out.println(stock.toString());
        }
        List<DbCard> dbCards = dbCardRepository.findAll();
        for(DbCard card : dbCards){
            System.out.println(card.toString());
        }
        List<DbNewsCountry> dbNewsCountriesList = dbNewsCountryRepository.findAll();
        for(DbNewsCountry dbNewsCountry: dbNewsCountriesList){
            System.out.println(dbNewsCountry.getNewsCountryId()+" "
                    +dbNewsCountry.getDbNews().toString()+ " "+ dbNewsCountry.getDbCountry()+" "
            + dbNewsCountry.getEffect());
        }
        List<DbNewsStock> dbNewsStocks = dbNewsStockRepository.findAll();
        for(DbNewsStock dbNewsStock:dbNewsStocks){
            System.out.println(dbNewsStock.getNewsStockId().toString() +" " +
                    dbNewsStock.getDbNews().toString() +" "+ dbNewsStock.getDbStock().toString() + " "
            + dbNewsStock.getEffect().toString());
        }


    }

    public void setInfo (){
        List<DbCountry> countries = dbCountryRespository.findAll();
        List<DbNews> news = dbNewsRepository.findAll();
        List<DbStock> stocks = dbStockRepository.findAll();
        List<DbCard> dbCards = dbCardRepository.findAll();




        gameInfoRepository.createGameRoom();


    }


}
