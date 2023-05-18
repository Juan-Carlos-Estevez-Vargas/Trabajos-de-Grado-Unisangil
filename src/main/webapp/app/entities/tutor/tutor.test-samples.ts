import dayjs from 'dayjs/esm';

import { ITutor, NewTutor } from './tutor.model';

export const sampleWithRequiredData: ITutor = {
  id: 88430,
};

export const sampleWithPartialData: ITutor = {
  id: 97787,
  lastName: 'Corona',
  email: 'Olivia_Terrazas54@yahoo.com',
};

export const sampleWithFullData: ITutor = {
  id: 73652,
  firstName: 'Andrea',
  lastName: 'Avilés',
  email: 'Marisol_Domnguez@gmail.com',
  phoneNumber: '1080p',
  hireDate: dayjs('2023-05-03T16:44'),
};

export const sampleWithNewData: NewTutor = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
