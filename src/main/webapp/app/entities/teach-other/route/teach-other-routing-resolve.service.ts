import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITeachOther } from '../teach-other.model';
import { TeachOtherService } from '../service/teach-other.service';

const teachOtherResolve = (route: ActivatedRouteSnapshot): Observable<null | ITeachOther> => {
  const id = route.params['id'];
  if (id) {
    return inject(TeachOtherService)
      .find(id)
      .pipe(
        mergeMap((teachOther: HttpResponse<ITeachOther>) => {
          if (teachOther.body) {
            return of(teachOther.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default teachOtherResolve;
