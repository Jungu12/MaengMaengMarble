 package maengmaeng.gamelogicservice.gameRoom.service;

 import maengmaeng.gamelogicservice.gameRoom.domain.db.DbCountry;
 import maengmaeng.gamelogicservice.gameRoom.repository.DbCountryRespository;
 import maengmaeng.gamelogicservice.util.RedisSubscriber;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.data.redis.listener.RedisMessageListenerContainer;
 import org.springframework.stereotype.Service;

 import lombok.RequiredArgsConstructor;
 import maengmaeng.gamelogicservice.gameRoom.repository.GameRoomRepository;

 import java.util.List;

 @RequiredArgsConstructor
 @Service
 public class GameRoomService {
 	 private final GameRoomRepository gameRoomRepository;
     private final RedisSubscriber redisSubscriber;
     private final RedisMessageListenerContainer redisMessageListener;
     private final DbCountryRespository countryRespository;

     private final Logger logger = LoggerFactory.getLogger(getClass());

    /** 1. waiting room redis접근 user객체 4개 가져와서 내가 맘대로 랜덤 순서 정해서
     *  2. user 객체 순서 pl
     *  3.

    * */
    public void getCountries(){
        List<DbCountry> list = countryRespository.findAll();

        for(DbCountry country: list){
            System.out.println(country.toString());
        }


    }

    public void getStocks(){

    }

//     public void setGameInfos(String roomCode) {
// 		//DB에 저장된 뉴스 정보 가져와서 redis에 넣기
//     }
//     public void findNewsInfo(String roomCode) {
//
// 	 }
//
//
//
//     private final WaitingRoomRepository waitingRoomRepository;
//
//     /*
//         방 입장
//      */
//     public void enter(String roomCode, UserInfo userInfo) {
//         // 대기방(WaitingRoom) 정보에 사용자 추가하기
//         waitingRoomRepository.addNewMember(roomCode,userInfo);
//
//     }
//     /*
//         현재 대기방 정보 얻기
//      */
//     public WaitingRoom getWaitingRoomNow(String roomCode) {
//         return waitingRoomRepository.getWaitingRoomNow(roomCode);
//     }


 }
