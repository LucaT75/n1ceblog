import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExpert, getExpertIdentifier } from '../expert.model';

export type EntityResponseType = HttpResponse<IExpert>;
export type EntityArrayResponseType = HttpResponse<IExpert[]>;

@Injectable({ providedIn: 'root' })
export class ExpertService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/experts');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(expert: IExpert): Observable<EntityResponseType> {
    return this.http.post<IExpert>(this.resourceUrl, expert, { observe: 'response' });
  }

  update(expert: IExpert): Observable<EntityResponseType> {
    return this.http.put<IExpert>(`${this.resourceUrl}/${getExpertIdentifier(expert) as string}`, expert, { observe: 'response' });
  }

  partialUpdate(expert: IExpert): Observable<EntityResponseType> {
    return this.http.patch<IExpert>(`${this.resourceUrl}/${getExpertIdentifier(expert) as string}`, expert, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IExpert>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExpert[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addExpertToCollectionIfMissing(expertCollection: IExpert[], ...expertsToCheck: (IExpert | null | undefined)[]): IExpert[] {
    const experts: IExpert[] = expertsToCheck.filter(isPresent);
    if (experts.length > 0) {
      const expertCollectionIdentifiers = expertCollection.map(expertItem => getExpertIdentifier(expertItem)!);
      const expertsToAdd = experts.filter(expertItem => {
        const expertIdentifier = getExpertIdentifier(expertItem);
        if (expertIdentifier == null || expertCollectionIdentifiers.includes(expertIdentifier)) {
          return false;
        }
        expertCollectionIdentifiers.push(expertIdentifier);
        return true;
      });
      return [...expertsToAdd, ...expertCollection];
    }
    return expertCollection;
  }
}
