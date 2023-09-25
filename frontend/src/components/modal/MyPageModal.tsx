import { CharacterType } from '@/types/common/common.type';
import { changeNickname, checkNickname, getCharaterList } from '@apis/userApi';
import { ToastMessageState } from '@atom/toastAtom';
import { userState } from '@atom/userAtom';
import CButton from '@components/common/CButton';
import CModal from '@components/common/CModal';
import MyPageCharacterCard from '@components/mypage/MyPageCharacterCard';
import { images } from '@constants/images';
import useToastList from '@hooks/useToastList';
import { ChangeEvent, useCallback, useEffect, useRef, useState } from 'react';
import { useRecoilState, useSetRecoilState } from 'recoil';

type CreateRoomModalProps = {
  isOpenCreateRoomModal: boolean;
  handleMyPageModalClose: () => void;
};

const MyPageModal = ({
  isOpenCreateRoomModal,
  handleMyPageModalClose,
}: CreateRoomModalProps) => {
  const [isError, setIsError] = useState(false);
  const [isEdit, setIsEdit] = useState(false);
  const [nickname, setNickname] = useState('');
  const [errorMsg, setErrorMsg] = useState('');
  const [charaterList, setCharaterList] = useState<CharacterType[]>([]);
  const [seletedCharater, setSeletedCharater] = useState(0); // 현재 선택한 캐릭터 (캐릭터 id값 저장)
  const inputRef = useRef<HTMLInputElement>(null);
  const { show } = useToastList();
  const setToastMessage = useSetRecoilState(ToastMessageState);
  const [user, setUser] = useRecoilState(userState);

  const handleNickName = useCallback((e: ChangeEvent<HTMLInputElement>) => {
    setNickname(e.target.value);
  }, []);

  const onClickEdit = useCallback(() => {
    setIsEdit(true);
    inputRef.current?.focus();
  }, []);

  const saveNickName = useCallback(() => {
    setNickname(nickname);
    setIsEdit(false);
  }, [nickname]);

  const saveProfile = useCallback(() => {
    if (isError || isEdit) {
      setIsEdit(true);
      inputRef.current?.focus();
      return;
    }
    changeNickname(nickname);
    if (user) {
      setUser({
        ...user,
        nickname: nickname,
      });
    }
    handleMyPageModalClose();
    // 토스트 메시지 띄우기
    setToastMessage((prev) => {
      return {
        ...prev,
        success: '저장 완료',
      };
    });
    show('success');
  }, [
    handleMyPageModalClose,
    isEdit,
    isError,
    nickname,
    setToastMessage,
    setUser,
    show,
    user,
  ]);

  const selectCharater = useCallback((id: number) => {
    setSeletedCharater(id);
  }, []);

  // 캐릭터 리스트 불러오기
  useEffect(() => {
    getCharaterList().then((res) => {
      setCharaterList(res.data);
    });
    if (user) {
      setSeletedCharater(user.avatarId);
    }
  }, [user]);

  useEffect(() => {
    if (user) {
      setNickname(user.nickname);
    }
    setIsEdit(false);
    setIsError(false);
    setErrorMsg('');
  }, [isOpenCreateRoomModal, user]);

  useEffect(() => {
    if (nickname.length < 2) {
      setIsError(true);
      setErrorMsg('닉네임은 2 ~ 12자 사이로 입력해야합니다.');
      console.log('맹');

      return;
    }

    if (nickname.length > 0 && nickname.length <= 20) {
      setIsError(false);
      setErrorMsg('');
      console.log('맹맹');
    }

    // 중복 닉네임 체크
    const checkValid = setTimeout(() => {
      if (user?.nickname === nickname) return;
      checkNickname(nickname).then((res) => {
        if (res.data) {
          setIsError(true);
          setErrorMsg('중복된 아이디입니다.');
        } else {
          setIsError(false);
          setErrorMsg('');
        }
      });
    }, 1000);

    return () => {
      clearTimeout(checkValid);
    };
  }, [nickname, user?.nickname]);

  return (
    <CModal isOpen={isOpenCreateRoomModal} handleClose={handleMyPageModalClose}>
      <div className='flex p-[16px] min-w-[960px]'>
        <div className='min-w-[600px] w-[600px] px-[24px] py-[12px] h-[600px] flex-1 flex flex-col overflow-y-scroll overflow-x-hidden scrollbar gap-[70px]'>
          <div className='w-[600px] flex gap-[16px] flex-wrap'>
            {charaterList.map((character, index) => (
              <MyPageCharacterCard
                key={index}
                character={character}
                status={
                  !character.hasAvatar
                    ? 'lock'
                    : seletedCharater === character.avatarId
                    ? 'choice'
                    : 'possession'
                }
                selectCharater={selectCharater}
              />
            ))}
          </div>
        </div>
        <div className='min-w-[450px] flex flex-col relative ml-[32px] items-center'>
          <img
            className='h-[450px] w-full object-cover rounded-[12px]'
            src={
              charaterList.length
                ? charaterList[seletedCharater - 1].avatarImage
                : ''
            }
            alt='내 캐릭터'
          />
          {isError ? (
            <div className='w-full flex justify-end mt-[12px] items-center px-[12px]'>
              <img
                className='w-[16px] h-[16px] mr-[4px]'
                src={images.icon.error}
                alt='에러'
              />
              <p className='text-[#E30909] text-sm'>{errorMsg}</p>
            </div>
          ) : (
            <div className='mt-[32px]'></div>
          )}
          <div className='w-full h-[54px] mt-[4px] relative flex justify-center'>
            <input
              className='flex text-center w-full h-full rounded-[20px] font-semibold text-xl text-[#5F594B] outline-none'
              value={nickname}
              onChange={handleNickName}
              readOnly={isEdit ? false : true}
              maxLength={20}
              ref={inputRef}
            />
            {isEdit ? (
              isError ? (
                <img
                  className='absolute h-[32px] w-[32px] right-[16px] bottom-[12px] cursor-not-allowed'
                  src={images.icon.registrationInactive}
                  alt='저장불가능'
                />
              ) : (
                <button onClick={saveNickName}>
                  <img
                    className='absolute h-[32px] w-[32px] right-[16px] bottom-[12px] cursor-pointer'
                    src={images.icon.registrationActive}
                    alt='저장하기'
                  />
                </button>
              )
            ) : (
              <button onClick={onClickEdit}>
                <img
                  className='absolute h-[32px] w-[32px] right-[16px] bottom-[12px] cursor-pointer'
                  src={images.icon.edit}
                  alt='수정하기'
                />
              </button>
            )}
          </div>
          <div className='w-full h-[48px] mt-[16px] relative'>
            <div className='w-full h-full'>
              <CButton type='green' rounded={20} onClick={saveProfile}>
                <p className={`text-xl font-semibold text-[#FFFDF2]`}>
                  저장하기
                </p>
              </CButton>
            </div>
          </div>
        </div>
      </div>
    </CModal>
  );
};

export default MyPageModal;
