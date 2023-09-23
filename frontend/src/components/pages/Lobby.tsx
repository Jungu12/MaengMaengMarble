import { images } from '@constants/images';
import CreateRoomModal from '@components/modal/CreateRoomModal';
import { useCallback, useEffect, useRef, useState } from 'react';
import LobbyHeader from '@components/lobby/LobbyHeader';
import LobbyCharacterView from '@components/lobby/LobbyCharacterView';
import LobbyRoomListView from '@components/lobby/LobbyRoomListView';
import * as StompJs from '@stomp/stompjs';
import { activateClient, getClient } from '@utils/socket';
import MyPageModal from '@components/modal/MyPageModal';
import InviteModal from '@components/modal/InviteModal';
import CToastError from '@components/common/CToastError';
import CToastSuccess from '@components/common/CToastSuccess';

const Lobby = () => {
  const clientRef = useRef<StompJs.Client>();
  const [isOpenCreateRoomModal, setIsOpenCreateRoomModal] = useState(false);
  const [isOpenMyPageModal, setIsOpenMyPageModal] = useState(false);
  const [isOpenInviteModal, setIsOpenInviteModal] = useState(false);
  const [toastErrorMessage, setToastErrorMessage] = useState('');

  const toastInvalidInviteCode = useCallback(() => {
    setToastErrorMessage('존재하지 않는 초대코드입니다');
  }, []);

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

  const onClickInviteButton = useCallback(() => {
    setIsOpenInviteModal((prev) => !prev);
  }, []);

  const handleInviteModal = useCallback(() => {
    setIsOpenInviteModal(false);
  }, []);

  // 소켓 연결
  useEffect(() => {
    clientRef.current = getClient();
    activateClient(clientRef.current);
    clientRef.current.onConnect = () => {};
  }, []);

  return (
    <>
      <InviteModal
        isOpenInviteModal={isOpenInviteModal}
        handleInviteModalClose={handleInviteModal}
        toastInvalidInviteCode={toastInvalidInviteCode}
      />
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
        className='flex flex-col w-full h-full relative p-[45px] overflow-auto'
        style={{
          backgroundImage: `url(${images.lobby.background})`,
          backgroundSize: 'cover',
        }}
      >
        <LobbyHeader />

        <div className='flex flex-1 flex-row w-full items-center justify-between mt-5 overflow-auto'>
          <LobbyCharacterView
            name='상근시치'
            img={images.default.character}
            point='28,000'
            handleMyPageModal={onClickSettingButton}
          />
          <LobbyRoomListView
            onClickInviteButton={onClickInviteButton}
            onClickCreateRoomButton={onClickCreateRoomButton}
            clientRef={clientRef}
          />
        </div>
      </div>
      <CToastError text={toastErrorMessage} />
      <CToastSuccess text='입장 성공' />
    </>
  );
};

export default Lobby;
