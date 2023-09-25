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
import { motion } from 'framer-motion';
import { getRooms } from '@apis/lobbyApi';
import { RoomType } from '@/types/common/lobby.type';
import MyFriendModal from '@components/modal/MyFriendModal';

const Lobby = () => {
  const clientRef = useRef<StompJs.Client>();
  const [isOpenCreateRoomModal, setIsOpenCreateRoomModal] = useState(false);
  const [isOpenMyPageModal, setIsOpenMyPageModal] = useState(false);
  const [isOpenInviteModal, setIsOpenInviteModal] = useState(false);
  const [isOpenFriendModal, setIsOpenFriendModal] = useState(false);
  const [roomList, setRoomList] = useState<RoomType[]>([]);

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

  const onClickFriendButton = useCallback(() => {
    setIsOpenFriendModal((prev) => !prev);
  }, []);

  const handleFriendModalClose = useCallback(() => {
    setIsOpenFriendModal(false);
  }, []);

  // 소켓 연결
  useEffect(() => {
    clientRef.current = getClient();
    activateClient(clientRef.current);
    clientRef.current.onConnect = () => {
      getRooms().then((res: { waitingRooms: RoomType[] }) => {
        console.log(res.waitingRooms);
        setRoomList(res.waitingRooms);
      });
    };
  }, []);

  return (
    <>
      <InviteModal
        isOpenInviteModal={isOpenInviteModal}
        handleInviteModalClose={handleInviteModal}
      />
      <CreateRoomModal
        isOpenCreateRoomModal={isOpenCreateRoomModal}
        handleCreateRoomModalClose={handleCreateRoomModalClose}
      />
      <MyPageModal
        isOpenCreateRoomModal={isOpenMyPageModal}
        handleMyPageModalClose={handleMyPageModalClose}
      />
      <MyFriendModal
        isOpenFriendModal={isOpenFriendModal}
        handleFriendModalClose={handleFriendModalClose}
      />

      <motion.div
        className='flex flex-col w-full h-full relative p-[45px] overflow-auto'
        style={{
          backgroundImage: `url(${images.lobby.background})`,
          backgroundSize: 'cover',
        }}
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        exit={{ opacity: 0 }}
      >
        <LobbyHeader onClickFriendButton={onClickFriendButton} />

        <div className='flex flex-1 flex-row w-full items-center justify-between mt-5 overflow-auto'>
          <LobbyCharacterView
            name='상근시치'
            img={images.default.character}
            point='28,000'
            handleMyPageModal={onClickSettingButton}
          />
          <LobbyRoomListView
            roomList={roomList}
            onClickInviteButton={onClickInviteButton}
            onClickCreateRoomButton={onClickCreateRoomButton}
            clientRef={clientRef}
          />
        </div>
      </motion.div>
    </>
  );
};

export default Lobby;
