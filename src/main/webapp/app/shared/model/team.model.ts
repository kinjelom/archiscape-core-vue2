import { IUser } from '@/shared/model/user.model';

export interface ITeam {
  id?: string;
  description?: string | null;
  users?: IUser[];
}

export class Team implements ITeam {
  constructor(public id?: string, public description?: string | null, public users?: IUser[]) {}
}
