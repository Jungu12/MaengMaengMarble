import CButton from '@components/common/CButton';
import CModal from '@components/common/CModal';
import MyPageCharacterCard from '@components/mypage/MyPageCharacterCard';
import { images } from '@constants/images';
import { ChangeEvent, useCallback, useEffect, useState } from 'react';

type CreateRoomModalProps = {
  isOpenCreateRoomModal: boolean;
  handleMyPageModalClose: () => void;
  name: string;
};

const MyPageModal = ({
  isOpenCreateRoomModal,
  handleMyPageModalClose,
  name,
}: CreateRoomModalProps) => {
  const [isError, setIsError] = useState(false);
  const [isEdit, setIsEdit] = useState(false);
  const [nickname, setNickname] = useState(name);
  const [errorMsg, setErrorMsg] = useState('');
  // const [seletedCharater, setSeletedCharater] = useState(0); // 현재 선택한 캐릭터 (캐릭터 id값 저장)

  const handleNickName = useCallback((e: ChangeEvent<HTMLInputElement>) => {
    setNickname(e.target.value);
  }, []);

  const onClickEdit = useCallback(() => {
    setIsEdit(true);
  }, []);

  const saveNickName = useCallback(() => {
    setNickname(nickname);
    setIsEdit(false);
  }, [nickname]);

  useEffect(() => {
    console.log(nickname.length);

    if (nickname.length === 0) {
      setIsError(true);
      setErrorMsg('닉네임은 1 ~ 20자 사이로 입력해야합니다.');
    }

    if (nickname.length > 0 && nickname.length <= 20) {
      setIsError(false);
      setErrorMsg('');
    }
  }, [nickname]);

  return (
    <CModal isOpen={isOpenCreateRoomModal} handleClose={handleMyPageModalClose}>
      <div className='flex p-[16px] min-w-[960px]'>
        <div className='min-w-[600px] w-[600px] px-[24px] py-[12px] h-[600px] flex-1 flex flex-col overflow-y-scroll overflow-x-hidden scrollbar gap-[70px]'>
          <div className='w-[600px] flex gap-[16px]'>
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter1}
              alt=''
              name='푸바오'
              status='choice'
            />
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter2}
              alt=''
              name='깜찍이'
              status='lock'
            />
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter3}
              alt=''
              name='맹티즈'
              status='possession'
            />
          </div>

          <div className='min-w-[600px] flex gap-[16px]'>
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter1}
              alt=''
              name='푸바오'
              status='choice'
            />
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter2}
              alt=''
              name='깜찍이'
              status='lock'
            />
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter3}
              alt=''
              name='맹티즈'
              status='possession'
            />
          </div>

          <div className='min-w-[600px] flex gap-[16px]'>
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter1}
              alt=''
              name='푸바오'
              status='choice'
            />
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter2}
              alt=''
              name='깜찍이'
              status='lock'
            />
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter3}
              alt=''
              name='맹티즈'
              status='possession'
            />
          </div>

          <div className='min-w-[600px] flex gap-[16px]'>
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter1}
              alt=''
              name='푸바오'
              status='choice'
            />
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter2}
              alt=''
              name='깜찍이'
              status='lock'
            />
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter3}
              alt=''
              name='맹티즈'
              status='possession'
            />
          </div>

          <div className='min-w-[600px] flex gap-[16px]'>
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter1}
              alt=''
              name='푸바오'
              status='choice'
            />
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter2}
              alt=''
              name='깜찍이'
              status='lock'
            />
            <MyPageCharacterCard
              src={images.dummy.dummyCharacter3}
              alt=''
              name='맹티즈'
              status='possession'
            />
          </div>
        </div>
        <div className='min-w-[450px] flex flex-col relative ml-[32px] items-center'>
          <img
            className='h-[450px] w-full object-cover rounded-[12px]'
            src={images.dummy.dummyCharacter1}
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
              <CButton type='green' rounded={20}>
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
