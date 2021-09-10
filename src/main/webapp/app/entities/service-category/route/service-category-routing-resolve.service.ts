import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IServiceCategory, ServiceCategory } from '../service-category.model';
import { ServiceCategoryService } from '../service/service-category.service';

@Injectable({ providedIn: 'root' })
export class ServiceCategoryRoutingResolveService implements Resolve<IServiceCategory> {
  constructor(protected service: ServiceCategoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IServiceCategory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((serviceCategory: HttpResponse<ServiceCategory>) => {
          if (serviceCategory.body) {
            return of(serviceCategory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ServiceCategory());
  }
}
