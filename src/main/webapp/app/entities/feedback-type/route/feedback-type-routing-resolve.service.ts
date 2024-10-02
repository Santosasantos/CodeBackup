import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFeedbackType } from '../feedback-type.model';
import { FeedbackTypeService } from '../service/feedback-type.service';

const feedbackTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IFeedbackType> => {
  const id = route.params['id'];
  if (id) {
    return inject(FeedbackTypeService)
      .find(id)
      .pipe(
        mergeMap((feedbackType: HttpResponse<IFeedbackType>) => {
          if (feedbackType.body) {
            return of(feedbackType.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default feedbackTypeResolve;
