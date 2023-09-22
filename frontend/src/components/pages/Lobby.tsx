import { images } from '@constants/images';
import CreateRoomModal from '@components/modal/CreateRoomModal';
import { useCallback, useEffect, useRef, useState } from 'react';
import LobbyHeader from '@components/lobby/LobbyHeader';
import LobbyCharacterView from '@components/lobby/LobbyCharacterView';
import LobbyRoomListView from '@components/lobby/LobbyRoomListView';
import * as StompJs from '@stomp/stompjs';
import { activateClient, getClient } from '@utils/socket';
import MyPageModal from '@components/modal/MyPageModal';

const Lobby = () => {
  const clientRef = useRef<StompJs.Client>();
  const [isOpenCreateRoomModal, setIsOpenCreateRoomModal] = useState(false);
  const [isOpenMyPageModal, setIsOpenMyPageModal] = useState(false);

  const onClickCreateRoomButton = useCallback(() => {
    setIsOpenCreateRoomModal((prev) => !prev);
  }, []);

  const handleCreateRoomModalClose = useCallback(() => {
    setIsOpenCreateRoomModal(false);
  }, []);

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
    clientRef.current.onConnect = () => {};
  }, []);

  return (
    <>
      {/* {isOpenRoomModal && <CreateRoomModal closeModal={closeRoomModal} />} */}
      <CreateRoomModal
        isOpenCreateRoomModal={isOpenCreateRoomModal}
        handleCreateRoomModalClose={handleCreateRoomModalClose}
      />
      <MyPageModal
        name={'개멋있는 사람'}
        isOpenCreateRoomModal={isOpenMyPageModal}
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
            onClickCreateRoomButton={onClickCreateRoomButton}
            clientRef={clientRef}
          />
        </div>
      </div>
    </>
  );
};

export default Lobby;
