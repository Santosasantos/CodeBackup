import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExtraquestion } from '../extraquestion.model';
import { ExtraquestionService } from '../service/extraquestion.service';

const extraquestionResolve = (route: ActivatedRouteSnapshot): Observable<null | IExtraquestion> => {
  const id = route.params['id'];
  if (id) {
    return inject(ExtraquestionService)
      .find(id)
      .pipe(
        mergeMap((extraquestion: HttpResponse<IExtraquestion>) => {
          if (extraquestion.body) {
            return of(extraquestion.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default extraquestionResolve;
