import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFeedbackDetails } from '../feedback-details.model';
import { FeedbackDetailsService } from '../service/feedback-details.service';

const feedbackDetailsResolve = (route: ActivatedRouteSnapshot): Observable<null | IFeedbackDetails> => {
  const id = route.params['id'];
  if (id) {
    return inject(FeedbackDetailsService)
      .find(id)
      .pipe(
        mergeMap((feedbackDetails: HttpResponse<IFeedbackDetails>) => {
          if (feedbackDetails.body) {
            return of(feedbackDetails.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default feedbackDetailsResolve;
