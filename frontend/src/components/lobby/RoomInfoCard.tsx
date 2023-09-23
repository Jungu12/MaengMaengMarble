import { RoomType } from '@/types/common/lobby.type';
import { currentParticipantsNum, totalParticipantsNum } from '@utils/lobby';
// import { Client } from '@stomp/stompjs';
import { useCallback } from 'react';
import { useNavigate } from 'react-router-dom';

type RoomInfoProps = {
  room: RoomType;
};

const RoomInfoCard = ({ room }: RoomInfoProps) => {
  const navigation = useNavigate();

  const enterGameRoom = useCallback(() => {
    navigation(`/waiting-room/${room.code}`);
  }, [room, navigation]);

  return (
    <div className='flex flex-col w-full p-6 bg-primary-100 rounded-[40px]'>
      <p className='text-2xl font-extrabold text-text-100'>{room.title}</p>
      <div className='flex flex-row items-center mt-5 justify-between'>
        <p className='text-xl font-bold text-text-50'>
          {currentParticipantsNum(room.currentParticipants)} /{' '}
          {totalParticipantsNum(room.currentParticipants)}
        </p>
        <button
          onClick={enterGameRoom}
          className='px-6 py-1 text-xl font-bold bg-primary-dark100 text-white rounded-[40px]'
        >
          입장
        </button>
      </div>
    </div>
  );
};

export default RoomInfoCard;
