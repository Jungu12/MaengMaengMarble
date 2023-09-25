import { images } from '@constants/images';
import { addComma } from '@utils/format';
import { useCallback, useEffect, useState } from 'react';
import StoreCharacterCard from '@components/store/StoreCharacterCard';
import StoreOwnerView from '@components/store/StoreOwnerView';
import PurchaseModal from '@components/modal/PurchaseModal';
import useToastList from '@hooks/useToastList';
import { CharacterType } from '@/types/common/common.type';
import { getStoreInfo, purchaseCharacter } from '@apis/storeApi';
import { StoreInfoType } from '@/types/store/store.type';
import { ToastMessageState } from '@atom/toastAtom';
import { useSetRecoilState } from 'recoil';

const Store = () => {
  const [isOpenPurchaseModal, setIsOpenPurchaseModal] = useState(false);
  const [myMoney, setMyMoney] = useState(3000);
  const [characterList, setCharacterList] = useState<CharacterType[]>([]);
  const [selectCid, setSelectCid] = useState(-1);
  const { show } = useToastList();
  const setToastMessage = useSetRecoilState(ToastMessageState);

  const toastFailPurchase = useCallback(() => {
    setToastMessage((prev) => {
      return {
        ...prev,
        error: '구매에 실패하였습니다.',
      };
    });
    show('error');
  }, [setToastMessage, show]);

  const toastLessPoint = useCallback(() => {
    setToastMessage((prev) => {
      return {
        ...prev,
        error: '포인트가 부족합니다.',
      };
    });
    show('error');
  }, [setToastMessage, show]);

  const handlePurchaseModalOpen = useCallback(() => {
    setIsOpenPurchaseModal((prev) => !prev);
  }, []);

  const handlePurchaseModalClose = useCallback(() => {
    setIsOpenPurchaseModal(false);
  }, []);

  const handleSelectedCid = useCallback((cid: number) => {
    setSelectCid(cid);
  }, []);

  const resquestPurchase = useCallback(
    (cid: number) => {
      purchaseCharacter(cid)
        .then((res) => {
          setMyMoney(res.point);
          setCharacterList(res.avatarList);
          setToastMessage((prev) => {
            return {
              ...prev,
              success: '구매 성공',
            };
          });
          show('success');
        })
        .catch(() => {
          toastFailPurchase();
        });
    },
    [setToastMessage, show, toastFailPurchase]
  );

  useEffect(() => {
    getStoreInfo().then((res: StoreInfoType) => {
      console.log(res);
      setMyMoney(res.point);
      setCharacterList(res.avatarList);
    });
  }, []);

  return (
    <>
      <PurchaseModal
        selectedCid={selectCid}
        handlePurchase={resquestPurchase}
        isOpenPurchaseModal={isOpenPurchaseModal}
        handlePurchaseModalClose={handlePurchaseModalClose}
      />
      <div
        className='flex flex-row w-full h-full justify-between relative'
        style={{
          backgroundImage: `url(${images.store.background})`,
          backgroundSize: 'cover',
        }}
      >
        <StoreOwnerView />

        <div className='basis-2/3 flex flex-col h-full items-end justify-between py-[45px] pr-[45px] relative'>
          <div className='flex flex-row items-center space-x-7 px-6 py-3 mb-[25px] bg-white bg-opacity-50 rounded-[40px]'>
            <img
              className='w-[40px] h-[40px] ml-auto mr-4'
              src={images.icon.point}
              alt='포인트 아이콘'
            />
            <p className='text-[30px] font-bold text-text-100'>
              {addComma(myMoney)}
            </p>
          </div>

          <div className='flex flex-col w-full h-full p-[25px] overflow-auto relative bg-primary-dark300 bg-opacity-70 rounded-[40px]'>
            <div className='grid grid-cols-4 gap-10 pr-[20px] w-full h-full relative scrollbar'>
              {characterList.map((character) => (
                <StoreCharacterCard
                  key={character.avatarId}
                  id={character.avatarId}
                  have={character.hasAvatar}
                  img={character.avatarImage}
                  name={character.avatarName}
                  point={character.avatarPrice}
                  onClick={
                    myMoney < character.avatarPrice
                      ? toastLessPoint
                      : handlePurchaseModalOpen
                  }
                  handleSelectedCid={handleSelectedCid}
                />
              ))}
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default Store;
