import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISkillDevelopmentType } from '../skill-development-type.model';
import { SkillDevelopmentTypeService } from '../service/skill-development-type.service';

const skillDevelopmentTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | ISkillDevelopmentType> => {
  const id = route.params['id'];
  if (id) {
    return inject(SkillDevelopmentTypeService)
      .find(id)
      .pipe(
        mergeMap((skillDevelopmentType: HttpResponse<ISkillDevelopmentType>) => {
          if (skillDevelopmentType.body) {
            return of(skillDevelopmentType.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default skillDevelopmentTypeResolve;
