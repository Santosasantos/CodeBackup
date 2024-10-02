import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { SkillDevelopmentTypeComponent } from './list/skill-development-type.component';
import { SkillDevelopmentTypeDetailComponent } from './detail/skill-development-type-detail.component';
import { SkillDevelopmentTypeUpdateComponent } from './update/skill-development-type-update.component';
import SkillDevelopmentTypeResolve from './route/skill-development-type-routing-resolve.service';

const skillDevelopmentTypeRoute: Routes = [
  {
    path: '',
    component: SkillDevelopmentTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SkillDevelopmentTypeDetailComponent,
    resolve: {
      skillDevelopmentType: SkillDevelopmentTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SkillDevelopmentTypeUpdateComponent,
    resolve: {
      skillDevelopmentType: SkillDevelopmentTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SkillDevelopmentTypeUpdateComponent,
    resolve: {
      skillDevelopmentType: SkillDevelopmentTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default skillDevelopmentTypeRoute;
