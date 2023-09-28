import { ChatMessageType } from '@/types/common/common.type';
import { useCallback, useEffect, useRef, useState } from 'react';

type Props = {
  chatList: ChatMessageType[];
  sendChatMessage: (msg: string) => void;
};

const WaitingRoomChatting = ({ chatList, sendChatMessage }: Props) => {
  const [input, setInput] = useState('');
  const scrollRef = useRef<HTMLDivElement>(null);

  const handleChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
    setInput(e.target.value);
  }, []);

  const scrollToBottom = useCallback(() => {
    if (scrollRef.current) {
      scrollRef.current.scrollTop = scrollRef.current.scrollHeight;
    }
  }, []);

  useEffect(() => {
    scrollToBottom();
  }, [chatList, scrollToBottom]);

  return (
    <div className='flex flex-col'>
      <div
        className=' px-[12px] py-[8px] w-[400px] h-[200px] rounded-[15px] overflow-y-auto scrollbar-chat'
        ref={scrollRef}
        style={{
          backgroundImage:
            ' linear-gradient(180deg, rgba(255, 255, 255, 0.00) 0%, rgba(255, 255, 255, 0.80) 100%)',
        }}
      >
        <div className='flex flex-col justify-end gap-[4px] font-[500]'>
          {chatList.map((chat, index) => (
            <span key={index}>{`${chat.sender} : ${chat.message}`}</span>
          ))}
        </div>
      </div>
      <input
        className='px-[8px] mt-[12px] w-[400px] h-[48px] rounded-lg border-2 border-white bg-opacity-50 bg-white focus:border-[#3AA1FF] outline-none'
        placeholder='플레이어들에게 하고 싶은 말을 적어주세요!'
        onKeyDown={(e) => {
          if (e.key === 'Enter' && !e.nativeEvent.isComposing) {
            if (input === '') return;
            e.preventDefault();
            sendChatMessage(input);
            setInput('');
          }
        }}
        value={input}
        onChange={handleChange}
      />
    </div>
  );
};

export default WaitingRoomChatting;
