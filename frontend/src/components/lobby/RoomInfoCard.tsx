import { RoomType } from '@/types/lobby/lobby.type';
import { ToastMessageState } from '@atom/toastAtom';
import useToastList from '@hooks/useToastList';
import { currentParticipantsNum, totalParticipantsNum } from '@utils/lobby';
// import { Client } from '@stomp/stompjs';
import { useCallback, useMemo } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';

type RoomInfoProps = {
  room: RoomType;
};

const RoomInfoCard = ({ room }: RoomInfoProps) => {
  const { show } = useToastList();
  const setToastMessage = useSetRecoilState(ToastMessageState);
  const navigation = useNavigate();
  const currentNum = useMemo(
    () => currentParticipantsNum(room.currentParticipants),
    [room.currentParticipants]
  );
  const totalNum = useMemo(
    () => totalParticipantsNum(room.currentParticipants),
    [room.currentParticipants]
  );

  const enterGameRoom = useCallback(() => {
    if (currentNum === totalNum) {
      setToastMessage((prev) => {
        return {
          ...prev,
          error: '인원초과로 입장이 불가능합니다.',
        };
      });
      show('error');
    } else {
      navigation(`/waiting-room/${room.code}`);
    }
  }, [currentNum, totalNum, setToastMessage, show, navigation, room.code]);

  return (
    <div className='flex flex-col w-full p-6 bg-primary-100 rounded-[40px]'>
      <p className='text-2xl font-extrabold text-text-100'>{room.title}</p>
      <div className='flex flex-row items-center mt-5 justify-between'>
        <p className='text-xl font-bold text-text-50'>
          {currentNum} / {totalNum}
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
