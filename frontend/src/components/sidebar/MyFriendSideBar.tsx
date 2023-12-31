import CSideBar from '@components/common/CSideBar';
import FriendInfoCard from '@components/lobby/FriendInfoCard';
import { useCallback, useState } from 'react';
import { motion } from 'framer-motion';
import CButton from '@components/common/CButton';
import useToastList from '@hooks/useToastList';
import { useRecoilState, useSetRecoilState } from 'recoil';
import { ToastMessageState } from '@atom/toastAtom';
import { addFriend, getFriendlist } from '@apis/friendApi';
import { friendState } from '@atom/userAtom';

type MyFriendSideBarProps = {
  isOpenFriendSideBar: boolean;
  handleFriendSideBarClose: () => void;
};

const MyFriendSideBar = ({
  isOpenFriendSideBar,
  handleFriendSideBarClose,
}: MyFriendSideBarProps) => {
  const [nickname, setNickname] = useState('');
  const { show } = useToastList();
  const setToastMessage = useSetRecoilState(ToastMessageState);
  const [friendList, setFriendList] = useRecoilState(friendState);

  const onChange = (event: React.FormEvent<HTMLInputElement>) => {
    const { currentTarget: nickname } = event;
    if (nickname.value.length <= 12) {
      setNickname(nickname.value);
    }
  };

  const onClickAddFriend = useCallback(() => {
    if (nickname.length <= 0) {
      setToastMessage((prev) => {
        return {
          ...prev,
          error: '닉네임을 입력하세요.',
        };
      });
      show('error');
    } else {
      addFriend(nickname)
        .then(() => {
          getFriendlist().then((res) => {
            setFriendList(res);
          });
          setToastMessage((prev) => {
            return {
              ...prev,
              success: '친구로 등록되었습니다.',
            };
          });
          show('success');
        })
        .catch((res) => {
          console.log(res);

          setToastMessage((prev) => {
            return {
              ...prev,
              error: '닉네임을 확인해주세요.',
            };
          });
          show('error');
        });
    }
    console.log(nickname);
  }, [nickname, setFriendList, setToastMessage, show]);

  const handleClose = useCallback(() => {
    setNickname('');
    handleFriendSideBarClose();
  }, [handleFriendSideBarClose]);

  if (!friendList) return;

  return (
    <CSideBar isOpen={isOpenFriendSideBar} handleClose={handleClose}>
      <div className='w-[450px] h-full overflow-auto flex flex-col items-center justify-center'>
        <p className='text-[28px] font-black text-text-100 mb-[20px]'>
          친구 목록
        </p>
        <div className='flex flex-col flex-1 w-full p-[20px] mb-[20px] overflow-auto bg-primary-dark300 bg-opacity-90 rounded-[40px]'>
          <div className='pr-[20px] w-full h-full relative scrollbar space-y-[20px]'>
            {friendList.map((friend) => (
              <FriendInfoCard
                key={friend.userId}
                id={friend.userId}
                name={friend.nickname}
                img={friend.character}
              />
            ))}
          </div>
        </div>
        <div className='flex flex-row w-full'>
          <div className='flex-1 flex flex-row items-center bg-primary-light100 rounded-[10px] px-[25px] '>
            <input
              className='text-text-100 text-[18px] font-extrabold outline-none placeholder:text-text-hint hover:border-transparent bg-primary-light100'
              value={nickname}
              onChange={onChange}
              type='text'
              placeholder='닉네임을 입력해주세요!'
            />
          </div>
          <motion.div
            whileHover={{ scale: 1.0 }}
            whileTap={{ scale: 0.9 }}
            transition={{ type: 'spring', stiffness: 140, damping: 20 }}
            className='w-[80px] h-[50px] ml-[15px] mr-[6px] mb-[5px]'
          >
            <CButton type='green' onClick={onClickAddFriend} rounded={10}>
              <p className='text-[16px] font-black text-primary-100'>
                친구 추가
              </p>
            </CButton>
          </motion.div>
        </div>
      </div>
    </CSideBar>
  );
};

export default MyFriendSideBar;
