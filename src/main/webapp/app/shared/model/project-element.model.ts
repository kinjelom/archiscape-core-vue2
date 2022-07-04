import { IProject } from '@/shared/model/project.model';
import { IElementConfiguration } from '@/shared/model/element-configuration.model';
import { IProjectView } from '@/shared/model/project-view.model';

import { C4ElementType } from '@/shared/model/enumerations/c-4-element-type.model';
export interface IProjectElement {
  id?: number;
  name?: string | null;
  documentation?: string | null;
  technology?: string | null;
  c4type?: C4ElementType;
  landscapeElementId?: string | null;
  extElementId?: string | null;
  extSourceElementId?: string | null;
  extTargetElementId?: string | null;
  project?: IProject;
  elementConfiguration?: IElementConfiguration | null;
  projectViews?: IProjectView[];
}

export class ProjectElement implements IProjectElement {
  constructor(
    public id?: number,
    public name?: string | null,
    public documentation?: string | null,
    public technology?: string | null,
    public c4type?: C4ElementType,
    public landscapeElementId?: string | null,
    public extElementId?: string | null,
    public extSourceElementId?: string | null,
    public extTargetElementId?: string | null,
    public project?: IProject,
    public elementConfiguration?: IElementConfiguration | null,
    public projectViews?: IProjectView[]
  ) {}
}
