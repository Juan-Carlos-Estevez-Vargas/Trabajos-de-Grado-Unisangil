import { IModality, NewModality } from './modality.model';

export const sampleWithRequiredData: IModality = {
  id: 99824,
};

export const sampleWithPartialData: IModality = {
  id: 62966,
  description: 'Croacia Mascotas card',
};

export const sampleWithFullData: IModality = {
  id: 36288,
  name: 'Madera Teclado virtual',
  description: 'feed',
  document: 'Buckinghamshire Regional Central',
};

export const sampleWithNewData: NewModality = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
