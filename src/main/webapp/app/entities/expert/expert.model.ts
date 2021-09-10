export interface IExpert {
  id?: string;
  shortBio?: string | null;
  expertise?: string | null;
  rating?: number | null;
  reviews?: number | null;
  candidatureVotes?: number | null;
  candidatureEndTime?: string | null;
  candidatureStakedAmount?: number | null;
}

export class Expert implements IExpert {
  constructor(
    public id?: string,
    public shortBio?: string | null,
    public expertise?: string | null,
    public rating?: number | null,
    public reviews?: number | null,
    public candidatureVotes?: number | null,
    public candidatureEndTime?: string | null,
    public candidatureStakedAmount?: number | null
  ) {}
}

export function getExpertIdentifier(expert: IExpert): string | undefined {
  return expert.id;
}
