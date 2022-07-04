export interface ITextFileContent {
  id?: string;
  version?: number;
  insertDate?: Date;
  fileName?: string | null;
  content?: string;
}

export class TextFileContent implements ITextFileContent {
  constructor(
    public id?: string,
    public version?: number,
    public insertDate?: Date,
    public fileName?: string | null,
    public content?: string
  ) {}
}
