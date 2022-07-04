import { ITeam } from '@/shared/model/team.model';

export interface IElementConfiguration {
  id?: string;
  name?: string;
  documentation?: string;
  technology?: string;
  eolDate?: Date | null;
  team?: ITeam;
}

export class ElementConfiguration implements IElementConfiguration {
  constructor(
    public id?: string,
    public name?: string,
    public documentation?: string,
    public technology?: string,
    public eolDate?: Date | null,
    public team?: ITeam
  ) {}
}
