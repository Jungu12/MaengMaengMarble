import { images } from '@constants/images';
import NewRoomModal from '@components/lobby/NewRoomModal';
import { useState } from 'react';
import LobbyHeader from '@components/lobby/LobbyHeader';
import LobbyCharacterView from '@components/lobby/LobbyCharacterView';
import LobbyRoomListView from '@components/lobby/LobbyRoomListView';

const Lobby = () => {
  const [isOpenNewRoomModal, setIsOpenNewRoomModal] = useState(false);

  return (
    <>
      {/* {isOpenRoomModal && <NewRoomModal closeModal={closeRoomModal} />} */}
      <NewRoomModal
        isOpenNewRoomModal={isOpenNewRoomModal}
        setIsOpenNewRoomModal={setIsOpenNewRoomModal}
      />
      <div
        className='flex flex-col w-full h-full overflow-hidden relative p-[45px]'
        style={{
          backgroundImage: `url(${images.lobby.background})`,
          backgroundSize: 'cover',
        }}
      >
        <LobbyHeader />

        <div className='flex flex-1 flex-row w-full items-center justify-between mt-5'>
          <LobbyCharacterView
            name='상근시치'
            img={images.default.character}
            point='28,000'
          />
          <LobbyRoomListView
            isOpenNewRoomModal={isOpenNewRoomModal}
            setIsOpenNewRoomModal={setIsOpenNewRoomModal}
          />
        </div>
      </div>
    </>
  );
};

export default Lobby;
