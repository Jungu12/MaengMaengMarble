import {
  CharacterType,
  ChatMessageType,
  ParticipantsType,
  WSResponseType,
} from '@/types/common/common.type';
import { RoomType } from '@/types/lobby/lobby.type';
import { getCharaterList } from '@apis/userApi';
import { userState } from '@atom/userAtom';
import WaitingRoomCharaterCard from '@components/watingRoom/WaitingRoomCharaterCard';
import WaitingRoomChatting from '@components/watingRoom/WaitingRoomChatting';
import { images } from '@constants/images';
import * as StompJs from '@stomp/stompjs';
import { activateClient, getClient } from '@utils/socket';
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
  const navigate = useNavigate();
  const user = useRecoilValue(userState);
  const { roomId } = useParams();
  const isReady = true;
  const client = useRef<StompJs.Client>();
  const [roomTitle, setRoomTitle] = useState('');
  const [inviteCode, setInviteCode] = useState('');
  const [userList, setUserList] = useState<ParticipantsType[]>([]);
  const [chatList, setChatList] = useState<ChatMessageType[]>([]);
  const [characterList, setCharacterList] = useState<CharacterType[]>([]);

  const onClickExitButton = useCallback(() => {
    navigate(-1);
  }, [navigate]);

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

  const onClickReady = useCallback(() => {
    client.current?.publish({
      destination: `/pub/waiting-rooms/ready/${roomId}`,
      body: JSON.stringify({
        userId: user?.userId,
        nickname: user?.nickname,
        characterId: user?.avatarId,
      }),
    });
  }, [roomId, user?.avatarId, user?.nickname, user?.userId]);

  // 캐릭터 리스트 불러오기
  useEffect(() => {
    getCharaterList().then((res) => setCharacterList(res.data));
  }, []);

  useEffect(() => {
    client.current = getClient();
    activateClient(client.current);
    client.current.onConnect = () => {
      if (client.current) {
        // 방 구독 하기
        client.current.subscribe(`/sub/waiting-rooms/${roomId}`, (res) => {
          const response: WSResponseType<RoomType> = JSON.parse(res.body);
          if (response.type === 'waitingRoom') {
            const { title, code, currentParticipant } = response.data;
            setRoomTitle(title);
            setInviteCode(code);
            setUserList(currentParticipant);
          }
          console.log(JSON.parse(res.body));
        });

        // 방 정보 얻어오기
        client.current.publish({
          destination: `/pub/waiting-rooms/${roomId}`,
          body: JSON.stringify({
            userId: user?.userId,
            nickname: user?.nickname,
            characterId: user?.avatarId,
          }),
        });
        client.current.subscribe(`/sub/chats/${roomId}`, (res) => {
          const response: WSResponseType<ChatMessageType> = JSON.parse(
            res.body
          );
          // 채팅 리스트 추가
          setChatList((prev) => [...prev, response.data]);
          console.log(JSON.parse(res.body));
        });
      }
    };
  }, [roomId, user?.avatarId, user?.nickname, user?.userId]);

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
      <div className='flex items-center w-full h-[80px] border-b-2 border-white/80 bg-blue-400/40 shadow-2xl'>
        <p className='font-extrabold text-[36px] text-white ml-[24px]'>
          {roomTitle}
        </p>
        <div className='flex items-center'>
          <img
            className='w-[28px] h-[28px] ml-[80px]'
            src={images.waitingRoom.mail}
            alt='초대코드'
          />
          <span className='ml-[20px] text-white font-extrabold text-[20px]'>
            {inviteCode}
          </span>
          <motion.img
            className='ml-[12px] w-[32px] h-[32px] cursor-pointer'
            src={images.waitingRoom.copy}
            alt='복사'
            whileHover={{ scale: 1.2 }}
            whileTap={{ scale: 0.9 }}
          />
        </div>
        <button className='ml-auto mr-[12px]' onClick={onClickExitButton}>
          <img
            className='w-[56px] h-[56px] cursor-pointer'
            src={images.waitingRoom.exit}
            alt='나가기'
          />
        </button>
      </div>
      <motion.div
        initial='start'
        animate='end'
        variants={BoxAnimation}
        className='flex justify-around h-full'
      >
        {characterList.length > 0 &&
          userList.map((user, index) => (
            <WaitingRoomCharaterCard
              key={index}
              name={user.nickname}
              avaterUrl={characterList[user.characterId - 1].avatarImageNoBg}
              isReady={user.ready}
              isManager={user.userId === userList[0].userId}
              manager={userList[0].userId}
              isClose={user.closed}
              animation={InnerAnimation}
            />
          ))}
        {/* <WaitingRoomCharaterCard
          name={'상근시치'}
          avaterUrl={images.dummy.dummy1}
          isReady={false}
          isManager={true}
          isClose={false}
          animation={InnerAnimation}
        />
        <WaitingRoomCharaterCard
          name={'215'}
          avaterUrl={images.dummy.dummy2}
          isReady={true}
          isManager={false}
          isClose={false}
          animation={InnerAnimation}
        />
        <WaitingRoomCharaterCard
          name={''}
          avaterUrl={''}
          isReady={false}
          isManager={false}
          isClose={true}
          animation={InnerAnimation}
        />
        <WaitingRoomCharaterCard
          name={'기므나'}
          avaterUrl={images.dummy.dummy3}
          isReady={false}
          isManager={false}
          isClose={false}
          animation={InnerAnimation}
        /> */}
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
        <button onClick={onClickReady}>
          <img
            className='h-[100px]'
            src={
              isReady
                ? images.waitingRoom.readyButton
                : images.waitingRoom.cancelButton
            }
            alt='button'
          />
        </button>
      </motion.div>
    </motion.div>
  );
};

export default WaitingRoom;
