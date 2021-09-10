import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IServiceCategory, getServiceCategoryIdentifier } from '../service-category.model';

export type EntityResponseType = HttpResponse<IServiceCategory>;
export type EntityArrayResponseType = HttpResponse<IServiceCategory[]>;

@Injectable({ providedIn: 'root' })
export class ServiceCategoryService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/service-categories');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(serviceCategory: IServiceCategory): Observable<EntityResponseType> {
    return this.http.post<IServiceCategory>(this.resourceUrl, serviceCategory, { observe: 'response' });
  }

  update(serviceCategory: IServiceCategory): Observable<EntityResponseType> {
    return this.http.put<IServiceCategory>(
      `${this.resourceUrl}/${getServiceCategoryIdentifier(serviceCategory) as string}`,
      serviceCategory,
      { observe: 'response' }
    );
  }

  partialUpdate(serviceCategory: IServiceCategory): Observable<EntityResponseType> {
    return this.http.patch<IServiceCategory>(
      `${this.resourceUrl}/${getServiceCategoryIdentifier(serviceCategory) as string}`,
      serviceCategory,
      { observe: 'response' }
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IServiceCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IServiceCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addServiceCategoryToCollectionIfMissing(
    serviceCategoryCollection: IServiceCategory[],
    ...serviceCategoriesToCheck: (IServiceCategory | null | undefined)[]
  ): IServiceCategory[] {
    const serviceCategories: IServiceCategory[] = serviceCategoriesToCheck.filter(isPresent);
    if (serviceCategories.length > 0) {
      const serviceCategoryCollectionIdentifiers = serviceCategoryCollection.map(
        serviceCategoryItem => getServiceCategoryIdentifier(serviceCategoryItem)!
      );
      const serviceCategoriesToAdd = serviceCategories.filter(serviceCategoryItem => {
        const serviceCategoryIdentifier = getServiceCategoryIdentifier(serviceCategoryItem);
        if (serviceCategoryIdentifier == null || serviceCategoryCollectionIdentifiers.includes(serviceCategoryIdentifier)) {
          return false;
        }
        serviceCategoryCollectionIdentifiers.push(serviceCategoryIdentifier);
        return true;
      });
      return [...serviceCategoriesToAdd, ...serviceCategoryCollection];
    }
    return serviceCategoryCollection;
  }
}
