import dayjs from 'dayjs/esm';

export interface ITutor {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  hireDate?: dayjs.Dayjs | null;
}

export type NewTutor = Omit<ITutor, 'id'> & { id: null };
