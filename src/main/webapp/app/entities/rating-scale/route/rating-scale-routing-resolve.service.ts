import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRatingScale } from '../rating-scale.model';
import { RatingScaleService } from '../service/rating-scale.service';

const ratingScaleResolve = (route: ActivatedRouteSnapshot): Observable<null | IRatingScale> => {
  const id = route.params['id'];
  if (id) {
    return inject(RatingScaleService)
      .find(id)
      .pipe(
        mergeMap((ratingScale: HttpResponse<IRatingScale>) => {
          if (ratingScale.body) {
            return of(ratingScale.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default ratingScaleResolve;
