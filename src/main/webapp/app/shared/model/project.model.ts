import { ILandscape } from '@/shared/model/landscape.model';
import { ITeam } from '@/shared/model/team.model';

export interface IProject {
  id?: number;
  name?: string;
  description?: string | null;
  extProjectId?: string | null;
  active?: boolean;
  srcUrl?: string | null;
  contentStoreUrl?: string | null;
  landscape?: ILandscape;
  team?: ITeam;
}

export class Project implements IProject {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public extProjectId?: string | null,
    public active?: boolean,
    public srcUrl?: string | null,
    public contentStoreUrl?: string | null,
    public landscape?: ILandscape,
    public team?: ITeam
  ) {
    this.active = this.active ?? false;
  }
}
