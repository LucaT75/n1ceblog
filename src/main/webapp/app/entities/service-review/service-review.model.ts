export interface IServiceReview {
  id?: string;
  serviceId?: string | null;
  userId?: string | null;
  rating?: number | null;
  comment?: string | null;
  publishingTime?: string | null;
}

export class ServiceReview implements IServiceReview {
  constructor(
    public id?: string,
    public serviceId?: string | null,
    public userId?: string | null,
    public rating?: number | null,
    public comment?: string | null,
    public publishingTime?: string | null
  ) {}
}

export function getServiceReviewIdentifier(serviceReview: IServiceReview): string | undefined {
  return serviceReview.id;
}
