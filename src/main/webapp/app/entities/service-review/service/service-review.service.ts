import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IServiceReview, getServiceReviewIdentifier } from '../service-review.model';

export type EntityResponseType = HttpResponse<IServiceReview>;
export type EntityArrayResponseType = HttpResponse<IServiceReview[]>;

@Injectable({ providedIn: 'root' })
export class ServiceReviewService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/service-reviews');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(serviceReview: IServiceReview): Observable<EntityResponseType> {
    return this.http.post<IServiceReview>(this.resourceUrl, serviceReview, { observe: 'response' });
  }

  update(serviceReview: IServiceReview): Observable<EntityResponseType> {
    return this.http.put<IServiceReview>(`${this.resourceUrl}/${getServiceReviewIdentifier(serviceReview) as string}`, serviceReview, {
      observe: 'response',
    });
  }

  partialUpdate(serviceReview: IServiceReview): Observable<EntityResponseType> {
    return this.http.patch<IServiceReview>(`${this.resourceUrl}/${getServiceReviewIdentifier(serviceReview) as string}`, serviceReview, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IServiceReview>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IServiceReview[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addServiceReviewToCollectionIfMissing(
    serviceReviewCollection: IServiceReview[],
    ...serviceReviewsToCheck: (IServiceReview | null | undefined)[]
  ): IServiceReview[] {
    const serviceReviews: IServiceReview[] = serviceReviewsToCheck.filter(isPresent);
    if (serviceReviews.length > 0) {
      const serviceReviewCollectionIdentifiers = serviceReviewCollection.map(
        serviceReviewItem => getServiceReviewIdentifier(serviceReviewItem)!
      );
      const serviceReviewsToAdd = serviceReviews.filter(serviceReviewItem => {
        const serviceReviewIdentifier = getServiceReviewIdentifier(serviceReviewItem);
        if (serviceReviewIdentifier == null || serviceReviewCollectionIdentifiers.includes(serviceReviewIdentifier)) {
          return false;
        }
        serviceReviewCollectionIdentifiers.push(serviceReviewIdentifier);
        return true;
      });
      return [...serviceReviewsToAdd, ...serviceReviewCollection];
    }
    return serviceReviewCollection;
  }
}
