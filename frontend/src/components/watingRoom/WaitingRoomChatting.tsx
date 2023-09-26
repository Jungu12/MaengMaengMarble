import { ChatMessageType } from '@/types/common/common.type';
import { useCallback, useEffect, useRef, useState } from 'react';
import * as StompJs from '@stomp/stompjs';
import { sendMessage } from '@utils/chatting';
import { userState } from '@atom/userAtom';
import { useRecoilValue } from 'recoil';

type Props = {
  chatList: ChatMessageType[];
  roomId: string;
  client: StompJs.Client | null;
};

const WaitingRoomChatting = ({ chatList, roomId, client }: Props) => {
  const [input, setInput] = useState('');
  const user = useRecoilValue(userState);
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

  if (!user) return;

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
          if (e.key === 'Enter') {
            if (input === '') return;
            sendMessage(client, input, roomId, user.nickname);

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
