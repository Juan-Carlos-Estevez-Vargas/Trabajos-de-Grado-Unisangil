import { State } from 'app/entities/enumerations/state.model';

import { IProposal, NewProposal } from './proposal.model';

export const sampleWithRequiredData: IProposal = {
  id: 69613,
};

export const sampleWithPartialData: IProposal = {
  id: 92798,
  title: 'Supervisor',
  state: State['APROBADA'],
  comments: 'La',
};

export const sampleWithFullData: IProposal = {
  id: 75067,
  title: 'Artesanal Borders',
  archive: 'invoice',
  state: State['CONDICIONADA'],
  comments: 'Denar Card Oficial',
  student: 'Diseñador',
};

export const sampleWithNewData: NewProposal = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
