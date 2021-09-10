import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExpertPlatformService, ExpertPlatformService } from '../expert-platform-service.model';
import { ExpertPlatformServiceService } from '../service/expert-platform-service.service';

@Injectable({ providedIn: 'root' })
export class ExpertPlatformServiceRoutingResolveService implements Resolve<IExpertPlatformService> {
  constructor(protected service: ExpertPlatformServiceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExpertPlatformService> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((expertPlatformService: HttpResponse<ExpertPlatformService>) => {
          if (expertPlatformService.body) {
            return of(expertPlatformService.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ExpertPlatformService());
  }
}
