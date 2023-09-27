import { ParticipantsType } from '@/types/common/common.type';

export const totalParticipantsNum = (
  participantsList: ParticipantsType[]
): number => {
  let participants = 4;

  participantsList.map((participant) => {
    if (participant.closed) participants -= 1;
  });

  return participants;
};

export const currentParticipantsNum = (
  participantsList: ParticipantsType[]
): number => {
  let participants = 0;

  participantsList.map((participant) => {
    if (!participant.nickname) participants += 1;
  });

  return participants;
};
