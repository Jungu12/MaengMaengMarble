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
import { RoomType } from '@/types/lobby/lobby.type';
import { useRecoilValue, useSetRecoilState } from 'recoil';
import { userState } from '@atom/userAtom';
import MyFriendSideBar from '@components/sidebar/MyFriendSideBar';
import { getFriendlist } from '@apis/friendApi';
import { FriendType } from '@/types/friend/friend.type';
import { ToastMessageState } from '@atom/toastAtom';
import useToastList from '@hooks/useToastList';
import { WSResponseType } from '@/types/common/common.type';

const Lobby = () => {
  const clientRef = useRef<StompJs.Client>();
  const user = useRecoilValue(userState);
  const [isOpenCreateRoomModal, setIsOpenCreateRoomModal] = useState(false);
  const [isOpenMyPageModal, setIsOpenMyPageModal] = useState(false);
  const [isOpenInviteModal, setIsOpenInviteModal] = useState(false);
  const [isOpenFriendSideBar, setIsOpenFriendSideBar] = useState(false);
  const [roomList, setRoomList] = useState<RoomType[]>([]);
  const [friendList, setFriendList] = useState<FriendType[]>([]);
  const { show } = useToastList();
  const setToastMessage = useSetRecoilState(ToastMessageState);

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
    setIsOpenFriendSideBar((prev) => !prev);
  }, []);

  const handleFriendSideBarClose = useCallback(() => {
    setIsOpenFriendSideBar(false);
  }, []);

  // 소켓 연결
  useEffect(() => {
    clientRef.current = getClient();
    activateClient(clientRef.current);
    clientRef.current.onConnect = () => {
      getRooms().then((res: { waitingRooms: RoomType[] }) => {
        console.log(res.waitingRooms);
        setRoomList(res.waitingRooms);
        clientRef.current?.subscribe('/sub/lobby', (res) => {
          const response: WSResponseType<{ waitingRooms: RoomType[] }> =
            JSON.parse(res.body);

          if (response.type === 'lobby') {
            setRoomList(response.data.waitingRooms);
            console.log(response.data);
          }
          // console.log(response);
        });
      });
    };

    // 구독 취소
    return () => {
      clientRef.current?.unsubscribe('/sub/lobby');
    };
  }, []);

  useEffect(() => {
    getFriendlist()
      .then((res) => {
        setFriendList(res);
      })
      .catch(() => {
        setToastMessage((prev) => {
          return {
            ...prev,
            error: '친구목록을 불러오는데 실패하였습니다.',
          };
        });
        show('error');
      });
  }, [setToastMessage, show]);

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
      <MyFriendSideBar
        friendList={friendList}
        isOpenFriendSideBar={isOpenFriendSideBar}
        handleFriendSideBarClose={handleFriendSideBarClose}
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
          {user && (
            <LobbyCharacterView
              name={user.nickname}
              img={user.avatarImageNoBg}
              point={user.point}
              handleMyPageModal={onClickSettingButton}
            />
          )}
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
