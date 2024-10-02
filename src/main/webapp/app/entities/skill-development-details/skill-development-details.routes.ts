import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { SkillDevelopmentDetailsComponent } from './list/skill-development-details.component';
import { SkillDevelopmentDetailsDetailComponent } from './detail/skill-development-details-detail.component';
import { SkillDevelopmentDetailsUpdateComponent } from './update/skill-development-details-update.component';
import SkillDevelopmentDetailsResolve from './route/skill-development-details-routing-resolve.service';

const skillDevelopmentDetailsRoute: Routes = [
  {
    path: '',
    component: SkillDevelopmentDetailsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SkillDevelopmentDetailsDetailComponent,
    resolve: {
      skillDevelopmentDetails: SkillDevelopmentDetailsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SkillDevelopmentDetailsUpdateComponent,
    resolve: {
      skillDevelopmentDetails: SkillDevelopmentDetailsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SkillDevelopmentDetailsUpdateComponent,
    resolve: {
      skillDevelopmentDetails: SkillDevelopmentDetailsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default skillDevelopmentDetailsRoute;
