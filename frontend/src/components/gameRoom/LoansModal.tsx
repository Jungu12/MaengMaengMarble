import { AnimatePresence, motion } from 'framer-motion';
import { images } from '@constants/images';
import CButton from '@components/common/CButton';
import { useCallback, useState } from 'react';
import useToastList from '@hooks/useToastList';
import { useSetRecoilState } from 'recoil';
import { ToastMessageState } from '@atom/toastAtom';
import { addAmountUnit } from '@utils/game';

type Props = {
  isOpen: boolean;
  handleLoans: () => void;
};

const LoansModal = ({ isOpen, handleLoans }: Props) => {
  const [loanMoney, setLoanMoney] = useState(30000000);
  const { show } = useToastList();
  const setToastMessage = useSetRecoilState(ToastMessageState);

  const increaseMoney = useCallback(() => {
    if (loanMoney === 30000000) {
      setToastMessage((prev) => {
        return {
          ...prev,
          error: '최대 3000만원까지 대출 가능',
        };
      });
      show('error');
    } else {
      setLoanMoney((prev) => prev + 5000000);
    }
  }, [loanMoney, setToastMessage, show]);

  const decreaseMoney = useCallback(() => {
    if (loanMoney === 5000000) {
      setToastMessage((prev) => {
        return {
          ...prev,
          error: '최소 500만원부터 대출 가능',
        };
      });
      show('error');
    } else {
      setLoanMoney((prev) => prev - 5000000);
    }
  }, [loanMoney, setToastMessage, show]);

  const onClickLoanButton = useCallback(() => {
    handleLoans();
    console.log(loanMoney);
  }, [loanMoney]);

  return (
    <AnimatePresence>
      {isOpen && (
        <div
          className='absolute flex w-full h-full bg-black bg-opacity-50 items-center justify-center'
          style={{
            zIndex: 100,
          }}
        >
          <motion.div
            initial={{
              opacity: 0,
              scale: 0.75,
            }}
            animate={{
              opacity: 1,
              scale: 1,
              transition: {
                ease: 'easeOut',
                duration: 0.15,
              },
            }}
            exit={{
              opacity: 0,
              scale: 0.75,
              transition: {
                ease: 'easeIn',
                duration: 0.15,
              },
            }}
            className='flex flex-col w-fit h-fit px-[20px] pt-[20px] bg-primary-100 rounded-[20px] relative'
          >
            <button className='ml-auto' onClick={handleLoans}>
              <img
                className='w-10 h-10'
                src={images.button.close}
                alt='닫기 버튼'
              />
            </button>
            <p className='text-[40px] font-extrabold text-text-100 ml-auto mr-auto mt-4 mb-[30px]'>
              대출
            </p>
            <p className='text-[28px] font-extrabold text-text-100 ml-auto mr-auto mt-4 mb-[30px]'>
              얼마까지 알아보고 오셨어요?
            </p>
            <div className='flex flex-row relative'>
              <img
                className='w-[438px] h-[504px]'
                src={images.gameRoom.rushandcash}
                alt='러시앤캐시 캐릭터 이미지'
              />
              <div className='w-[330px] h-[504px] flex flex-col justify-between pb-[25px] pt-[80px] pr-[6px] ml-[20px] relative'>
                <div className='flex flex-col w-full'>
                  <div className='flex flex-row w-full items-center justify-between py-[10px] px-[30px] bg-primary-light100 rounded-[15px]'>
                    <p className='text-[28px] font-extrabold text-text-100'>
                      {addAmountUnit(loanMoney)}
                    </p>
                    <div className='flex flex-col'>
                      <button
                        className='w-[25px] h-[25px]'
                        onClick={increaseMoney}
                      >
                        <img src={images.icon.up} alt='플러스 버튼' />
                      </button>
                      <button
                        className='w-[25px] h-[25px]'
                        onClick={decreaseMoney}
                      >
                        <img src={images.icon.down} alt='마이너스 버튼' />
                      </button>
                    </div>
                  </div>
                  <p className='text-[9px] font-extrabold text-text-hint whitespace-pre-wrap p-[10px]'>
                    {`대출은 최대 3000만원까지 가능하며,\n한 바퀴 당 빌린 금액의 24%의 이자를 내야 합니다.\n단, 대출 원금은 해당 칸에 도착해야만 갚을 수 있음을 주의하시기 바랍니다.`}
                  </p>
                </div>
                <div className='flex flex-row items-center justify-between w-full'>
                  <motion.div
                    whileHover={{ scale: 1.1 }}
                    whileTap={{ scale: 0.9 }}
                    transition={{ type: 'spring', stiffness: 400, damping: 17 }}
                  >
                    <CButton
                      type='green'
                      onClick={onClickLoanButton}
                      width={150}
                      height={50}
                      rounded={15}
                    >
                      <p className='text-[22px] font-black text-primary-100'>
                        빌리기
                      </p>
                    </CButton>
                  </motion.div>
                  <motion.div
                    whileHover={{ scale: 1.1 }}
                    whileTap={{ scale: 0.9 }}
                    transition={{ type: 'spring', stiffness: 400, damping: 17 }}
                  >
                    <CButton
                      type='red'
                      onClick={handleLoans}
                      width={150}
                      height={50}
                      rounded={15}
                    >
                      <p className='text-[22px] font-black text-primary-100'>
                        나가기
                      </p>
                    </CButton>
                  </motion.div>
                </div>
              </div>
            </div>
          </motion.div>
        </div>
      )}
    </AnimatePresence>
  );
};

export default LoansModal;
