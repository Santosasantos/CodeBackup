import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExtraquestionAns } from '../extraquestion-ans.model';
import { ExtraquestionAnsService } from '../service/extraquestion-ans.service';

const extraquestionAnsResolve = (route: ActivatedRouteSnapshot): Observable<null | IExtraquestionAns> => {
  const id = route.params['id'];
  if (id) {
    return inject(ExtraquestionAnsService)
      .find(id)
      .pipe(
        mergeMap((extraquestionAns: HttpResponse<IExtraquestionAns>) => {
          if (extraquestionAns.body) {
            return of(extraquestionAns.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default extraquestionAnsResolve;
