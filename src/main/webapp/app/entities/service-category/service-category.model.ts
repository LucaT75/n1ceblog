export interface IServiceCategory {
  id?: string;
  title?: string | null;
  icon?: string | null;
  services?: string | null;
}

export class ServiceCategory implements IServiceCategory {
  constructor(public id?: string, public title?: string | null, public icon?: string | null, public services?: string | null) {}
}

export function getServiceCategoryIdentifier(serviceCategory: IServiceCategory): string | undefined {
  return serviceCategory.id;
}
