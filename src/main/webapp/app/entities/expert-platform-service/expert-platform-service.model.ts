export interface IExpertPlatformService {
  id?: string;
  title?: string | null;
  content?: string | null;
  expertId?: string | null;
  featuredImg?: string | null;
  category?: string | null;
  startingPrice?: number | null;
  publishingTime?: string | null;
}

export class ExpertPlatformService implements IExpertPlatformService {
  constructor(
    public id?: string,
    public title?: string | null,
    public content?: string | null,
    public expertId?: string | null,
    public featuredImg?: string | null,
    public category?: string | null,
    public startingPrice?: number | null,
    public publishingTime?: string | null
  ) {}
}

export function getExpertPlatformServiceIdentifier(expertPlatformService: IExpertPlatformService): string | undefined {
  return expertPlatformService.id;
}
