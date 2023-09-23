import * as StompJs from '@stomp/stompjs';

/**
 * 클라이언트 객체를 반환하는 함수
 * @returns Client 객체 반환
 */
export const getClient = () => {
  const client = new StompJs.Client({
    // brokerURL: 'ws://192.168.100.64:8080/api/maeng',
    brokerURL: 'ws://192.168.1.11:8080/api/maeng',
    connectHeaders: {
      login: '',
      passcode: 'password',
    },
    onConnect: () => {
      console.log('연결 됬습니다~');
    },
    debug: function (str) {
      console.log(str);
    },
    reconnectDelay: 5000, // 자동 재 연결
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
  });

  return client;
};

/**
 * 소켓을 활성화시킨다.
 * @param client
 */
export const activateClient = (client: StompJs.Client) => {
  client.activate();
};