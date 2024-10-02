import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISkillDevelopmentDetails } from '../skill-development-details.model';
import { SkillDevelopmentDetailsService } from '../service/skill-development-details.service';

const skillDevelopmentDetailsResolve = (route: ActivatedRouteSnapshot): Observable<null | ISkillDevelopmentDetails> => {
  const id = route.params['id'];
  if (id) {
    return inject(SkillDevelopmentDetailsService)
      .find(id)
      .pipe(
        mergeMap((skillDevelopmentDetails: HttpResponse<ISkillDevelopmentDetails>) => {
          if (skillDevelopmentDetails.body) {
            return of(skillDevelopmentDetails.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default skillDevelopmentDetailsResolve;
