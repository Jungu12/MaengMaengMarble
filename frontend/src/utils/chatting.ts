import * as StompJs from '@stomp/stompjs';

/**
 * 메시지를 전송한다.
 * @param {StompJs.Client} client - STOMP 클라이언트 객체
 * @param {string} msg - 보낼 메시지
 * @param {string} roomId - 방 코드
 * @param {string} userName - 보낸 사용자의 이름
 */
export const sendMessage = (
  client: StompJs.Client | null,
  msg: string,
  roomId: string,
  userName: string
) => {
  if (!client) return;

  client.publish({
    destination: `/pub/chats`,
    body: JSON.stringify({
      roomCode: roomId,
      sender: userName,
      message: msg,
    }),
  });
};
