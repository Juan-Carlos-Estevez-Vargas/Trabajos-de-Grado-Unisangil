import { State } from 'app/entities/enumerations/state.model';

export interface IProposal {
  id: number;
  title?: string | null;
  archive?: string | null;
  state?: State | null;
  comments?: string | null;
  student?: string | null;
}

export type NewProposal = Omit<IProposal, 'id'> & { id: null };
