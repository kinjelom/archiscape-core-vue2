export interface ILandscape {
  id?: string;
  description?: string | null;
}

export class Landscape implements ILandscape {
  constructor(public id?: string, public description?: string | null) {}
}
