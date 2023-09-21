import { images } from '@constants/images';
import NewRoomModal from '@components/lobby/NewRoomModal';
import { useCallback, useEffect, useRef, useState } from 'react';
import LobbyHeader from '@components/lobby/LobbyHeader';
import LobbyCharacterView from '@components/lobby/LobbyCharacterView';
import LobbyRoomListView from '@components/lobby/LobbyRoomListView';
import * as StompJs from '@stomp/stompjs';
import { activateClient, getClient } from '@utils/socket';
import MyPageModal from '@components/modal/MyPageModal';

const Lobby = () => {
  const clientRef = useRef<StompJs.Client>();
  const [isOpenNewRoomModal, setIsOpenNewRoomModal] = useState(false);
  const [isOpenMyPageModal, setIsOpenMyPageModal] = useState(false);

  const onClickSettingButton = useCallback(() => {
    setIsOpenMyPageModal((prev) => !prev);
  }, []);

  const handleMyPageModalClose = useCallback(() => {
    setIsOpenMyPageModal(false);
  }, []);

  // 소켓 연결
  useEffect(() => {
    clientRef.current = getClient();
    activateClient(clientRef.current);
  }, []);

  return (
    <>
      {/* {isOpenRoomModal && <NewRoomModal closeModal={closeRoomModal} />} */}
      <NewRoomModal
        isOpenNewRoomModal={isOpenNewRoomModal}
        setIsOpenNewRoomModal={setIsOpenNewRoomModal}
      />
      <MyPageModal
        isOpenNewRoomModal={isOpenMyPageModal}
        handleMyPageModalClose={handleMyPageModalClose}
      />
      <div
        className='flex flex-col w-full h-full overflow-hidden relative p-[45px]'
        style={{
          backgroundImage: `url(${images.lobby.background})`,
          backgroundSize: 'cover',
        }}
      >
        <LobbyHeader />

        <div className='flex flex-1 flex-row w-full items-center justify-between mt-5 '>
          <LobbyCharacterView
            name='상근시치'
            img={images.default.character}
            point='28,000'
            handleMyPageModal={onClickSettingButton}
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
