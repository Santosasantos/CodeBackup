import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFeedback } from '../feedback.model';
import { FeedbackService } from '../service/feedback.service';

const feedbackResolve = (route: ActivatedRouteSnapshot): Observable<null | IFeedback> => {
  const id = route.params['id'];
  if (id) {
    return inject(FeedbackService)
      .find(id)
      .pipe(
        mergeMap((feedback: HttpResponse<IFeedback>) => {
          if (feedback.body) {
            return of(feedback.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default feedbackResolve;
