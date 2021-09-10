import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IServiceReview, ServiceReview } from '../service-review.model';
import { ServiceReviewService } from '../service/service-review.service';

@Injectable({ providedIn: 'root' })
export class ServiceReviewRoutingResolveService implements Resolve<IServiceReview> {
  constructor(protected service: ServiceReviewService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IServiceReview> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((serviceReview: HttpResponse<ServiceReview>) => {
          if (serviceReview.body) {
            return of(serviceReview.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ServiceReview());
  }
}
