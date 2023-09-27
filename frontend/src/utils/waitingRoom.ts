import { ParticipantsType } from '@/types/common/common.type';

/**
 * 대기플레이어 목록 중 내 정보를 찾는다
 * @param {ParticipantsType} userList - 참가자 목록 리스트
 * @param {string} userId - 자기 아이디
 */
export function findMyData(userList: ParticipantsType[], userId: string) {
  for (const user of userList) {
    if (user.userId === userId) {
      return user;
    }
  }

  return null;
}
