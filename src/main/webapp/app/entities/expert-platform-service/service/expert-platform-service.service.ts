import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExpertPlatformService, getExpertPlatformServiceIdentifier } from '../expert-platform-service.model';

export type EntityResponseType = HttpResponse<IExpertPlatformService>;
export type EntityArrayResponseType = HttpResponse<IExpertPlatformService[]>;

@Injectable({ providedIn: 'root' })
export class ExpertPlatformServiceService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/expert-platform-services');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(expertPlatformService: IExpertPlatformService): Observable<EntityResponseType> {
    return this.http.post<IExpertPlatformService>(this.resourceUrl, expertPlatformService, { observe: 'response' });
  }

  update(expertPlatformService: IExpertPlatformService): Observable<EntityResponseType> {
    return this.http.put<IExpertPlatformService>(
      `${this.resourceUrl}/${getExpertPlatformServiceIdentifier(expertPlatformService) as string}`,
      expertPlatformService,
      { observe: 'response' }
    );
  }

  partialUpdate(expertPlatformService: IExpertPlatformService): Observable<EntityResponseType> {
    return this.http.patch<IExpertPlatformService>(
      `${this.resourceUrl}/${getExpertPlatformServiceIdentifier(expertPlatformService) as string}`,
      expertPlatformService,
      { observe: 'response' }
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IExpertPlatformService>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExpertPlatformService[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addExpertPlatformServiceToCollectionIfMissing(
    expertPlatformServiceCollection: IExpertPlatformService[],
    ...expertPlatformServicesToCheck: (IExpertPlatformService | null | undefined)[]
  ): IExpertPlatformService[] {
    const expertPlatformServices: IExpertPlatformService[] = expertPlatformServicesToCheck.filter(isPresent);
    if (expertPlatformServices.length > 0) {
      const expertPlatformServiceCollectionIdentifiers = expertPlatformServiceCollection.map(
        expertPlatformServiceItem => getExpertPlatformServiceIdentifier(expertPlatformServiceItem)!
      );
      const expertPlatformServicesToAdd = expertPlatformServices.filter(expertPlatformServiceItem => {
        const expertPlatformServiceIdentifier = getExpertPlatformServiceIdentifier(expertPlatformServiceItem);
        if (
          expertPlatformServiceIdentifier == null ||
          expertPlatformServiceCollectionIdentifiers.includes(expertPlatformServiceIdentifier)
        ) {
          return false;
        }
        expertPlatformServiceCollectionIdentifiers.push(expertPlatformServiceIdentifier);
        return true;
      });
      return [...expertPlatformServicesToAdd, ...expertPlatformServiceCollection];
    }
    return expertPlatformServiceCollection;
  }
}
