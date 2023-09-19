const WatingRoomChatting = () => {
  return (
    <div className='flex flex-col'>
      <div
        className='font-[500] px-[12px] py-[8px] w-[400px] h-[200px] rounded-[15px] flex flex-col justify-end'
        style={{
          backgroundImage:
            ' linear-gradient(180deg, rgba(255, 255, 255, 0.00) 0%, rgba(255, 255, 255, 0.80) 100%)',
        }}
      >
        <span>215 : ㅎㅇㅎㅇ</span>
        <span>상근시치 : 레디 좀 해주셈</span>
      </div>
      <input
        className='px-[8px] mt-[12px] w-[400px] h-[48px] rounded-lg border-2 border-white bg-opacity-50 bg-white focus:border-[#3AA1FF] outline-none'
        placeholder='플레이어들에게 하고 싶은 말을 적어주세요!'
      />
    </div>
  );
};

export default WatingRoomChatting;
