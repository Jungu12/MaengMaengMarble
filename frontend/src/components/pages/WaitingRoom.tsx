import {
  CharacterType,
  ChatMessageType,
  ParticipantsType,
  WSResponseType,
} from '@/types/common/common.type';
import { RoomType } from '@/types/lobby/lobby.type';
import { getCharacterList } from '@apis/userApi';

import { userState } from '@atom/userAtom';
import WaitingRoomCharaterCard from '@components/watingRoom/WaitingRoomCharaterCard';
import WaitingRoomChatting from '@components/watingRoom/WaitingRoomChatting';
import WaitingRoomHeader from '@components/watingRoom/WaitingRoomHeader';
import { images } from '@constants/images';
import * as StompJs from '@stomp/stompjs';
import { activateClient, getClient } from '@utils/socket';
import { findMyData } from '@utils/waitingRoom';
import { motion } from 'framer-motion';
import { useCallback, useEffect, useRef, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useRecoilValue } from 'recoil';

const BoxAnimation = {
  start: { scale: 0, opacity: 0.5 },
  end: {
    scale: 1,
    opacity: 1,
    transition: {
      duration: 0.5,
      type: 'spring',
      stiffness: 110,
      delayChildren: 1,
      staggerChildren: 0.5,
    },
  },
};

const InnerAnimation = {
  start: { opacity: 0, y: 10 },
  end: { opacity: 1, y: 0 },
};

const WaitingRoom = () => {
  const navigation = useNavigate();
  const user = useRecoilValue(userState);
  const { roomId } = useParams();
  const client = useRef<StompJs.Client>();
  const waitSub = useRef<StompJs.StompSubscription>();
  const chatSub = useRef<StompJs.StompSubscription>();
  const [roomTitle, setRoomTitle] = useState('');
  const [inviteCode, setInviteCode] = useState('');
  const [userList, setUserList] = useState<ParticipantsType[]>([]);
  const [chatList, setChatList] = useState<ChatMessageType[]>([]);
  const [characterList, setCharacterList] = useState<CharacterType[]>([]);

  const sendChatMessage = useCallback(
    (msg: string) => {
      console.log(msg);

      client.current?.publish({
        destination: `/pub/chats`,
        body: JSON.stringify({
          roomCode: roomId,
          sender: user?.nickname,
          message: msg,
        }),
      });
    },
    [roomId, user?.nickname]
  );

  const handleReady = useCallback(() => {
    client.current?.publish({
      destination: `/pub/waiting-rooms/ready/${roomId}`,
      body: JSON.stringify({
        userId: user?.userId,
        nickname: user?.nickname,
        characterId: user?.avatarId,
      }),
    });
  }, [roomId, user?.avatarId, user?.nickname, user?.userId]);

  const handleKick = useCallback(
    (nickName: string) => {
      console.log('[강퇴]', nickName);

      client.current?.publish({
        destination: `/pub/waiting-rooms/kick/${roomId}`,
        body: JSON.stringify({
          outUser: nickName,
        }),
      });
    },
    [roomId]
  );

  const handleExit = useCallback(() => {
    client.current?.publish({
      destination: `/pub/waiting-rooms/exit/${roomId}`,
      body: JSON.stringify({
        userId: user?.userId,
        nickname: user?.nickname,
        characterId: user?.avatarId,
      }),
    });
    // 구독 연결 끊기
    waitSub.current?.unsubscribe();
    console.log('방에서 나갔습니다');
  }, [roomId, user?.avatarId, user?.nickname, user?.userId]);

  const handleGameStart = useCallback(() => {
    console.log('게임 시작');

    client.current?.publish({
      destination: `/pub/waiting-rooms/start/${roomId}`,
    });
  }, [roomId]);

  // 캐릭터 리스트 불러오기
  useEffect(() => {
    getCharacterList().then((res) => setCharacterList(res.data));
  }, []);

  useEffect(() => {
    client.current = getClient();
    activateClient(client.current);
    client.current.onConnect = () => {
      if (client.current) {
        // 방 구독 하기
        waitSub.current = client.current.subscribe(
          `/sub/waiting-rooms/${roomId}`,
          (res) => {
            const response: WSResponseType<RoomType> = JSON.parse(res.body);
            if (response.type === 'waitingRoom') {
              const { title, code, currentParticipants } = response.data;
              setRoomTitle(title);
              setInviteCode(code);
              setUserList(currentParticipants);
            }
            // 모두 레디가 완료되고 게임 시작 버튼을 클릭한 경우
            if (response.type === 'gameStart') {
              console.log('게임 시작!!');
              waitSub.current?.unsubscribe();
              navigation(`/game-room/${roomId}`);
            }

            if (response.type === '방 폭파') {
              console.log('방장 나감');
              waitSub.current?.unsubscribe();
              navigation(`/lobby`);
            }
            console.log(JSON.parse(res.body));
          }
        );

        // 방 정보 얻어오기
        client.current.publish({
          destination: `/pub/waiting-rooms/${roomId}`,
          body: JSON.stringify({
            userId: user?.userId,
            nickname: user?.nickname,
            characterId: user?.avatarId,
          }),
        });
        chatSub.current = client.current.subscribe(
          `/sub/chats/${roomId}`,
          (res) => {
            const response: WSResponseType<ChatMessageType> = JSON.parse(
              res.body
            );
            // 채팅 리스트 추가
            setChatList((prev) => [...prev, response.data]);
            console.log(JSON.parse(res.body));
          }
        );
      }
    };

    // 연결 종료 시 로비로 이동 시킴
    client.current.onDisconnect = () => {
      navigation('/lobby');
    };

    // 방 나가기
    return () => {
      handleExit();
    };
  }, [
    handleExit,
    navigation,
    roomId,
    user?.avatarId,
    user?.nickname,
    user?.userId,
  ]);

  if (!user) return;

  return (
    <motion.div
      className='flex flex-col w-full h-full min-h-[700px] overflow-hidden relative'
      style={{
        backgroundImage: `url(${images.waitingRoom.background})`,
        backgroundSize: 'cover',
      }}
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      exit={{ opacity: 0 }}
    >
      <WaitingRoomHeader title={roomTitle} code={inviteCode} />
      <motion.div
        initial='start'
        animate='end'
        variants={BoxAnimation}
        className='flex justify-around h-full'
      >
        {characterList.length &&
          userList.length &&
          userList.map((user, index) => (
            <WaitingRoomCharaterCard
              key={index}
              name={user.nickname}
              avaterUrl={
                user.characterId > 0
                  ? characterList[user.characterId - 1].avatarImageNoBg
                  : null
              }
              isReady={user.ready}
              isManager={user.userId === userList[0].userId}
              manager={userList[0].userId}
              isClose={user.closed}
              animation={InnerAnimation}
              handleKick={handleKick}
            />
          ))}
      </motion.div>
      <div className='absolute bottom-[8px] left-[12px]'>
        <WaitingRoomChatting
          chatList={chatList}
          sendChatMessage={sendChatMessage}
        />
      </div>
      <motion.div
        className='z-10 cursor-pointer absolute bottom-[8px] left-[40%]'
        whileHover={{ scale: 1.1 }}
        whileTap={{ scale: 0.9 }}
        transition={{ type: 'spring', stiffness: 400, damping: 17 }}
      >
        {userList.length && userList[0].userId === user.userId ? (
          <button onClick={handleGameStart}>
            <img
              className='h-[100px]'
              src={images.waitingRoom.gameStartButton}
              alt='게임시작 버튼'
            />
          </button>
        ) : (
          <button onClick={handleReady}>
            <img
              className='h-[100px]'
              src={
                findMyData(userList, user.userId)?.ready
                  ? images.waitingRoom.cancelButton
                  : images.waitingRoom.readyButton
              }
              alt='button'
            />
          </button>
        )}
      </motion.div>
    </motion.div>
  );
};

export default WaitingRoom;
