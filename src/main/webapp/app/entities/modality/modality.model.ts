export interface IModality {
  id: number;
  name?: string | null;
  description?: string | null;
  document?: string | null;
}

export type NewModality = Omit<IModality, 'id'> & { id: null };
