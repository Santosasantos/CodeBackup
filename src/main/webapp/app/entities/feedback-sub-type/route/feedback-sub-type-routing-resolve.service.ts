import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFeedbackSubType } from '../feedback-sub-type.model';
import { FeedbackSubTypeService } from '../service/feedback-sub-type.service';

const feedbackSubTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IFeedbackSubType> => {
  const id = route.params['id'];
  if (id) {
    return inject(FeedbackSubTypeService)
      .find(id)
      .pipe(
        mergeMap((feedbackSubType: HttpResponse<IFeedbackSubType>) => {
          if (feedbackSubType.body) {
            return of(feedbackSubType.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default feedbackSubTypeResolve;
