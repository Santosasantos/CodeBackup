import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ExtraquestionAnsComponent } from './list/extraquestion-ans.component';
import { ExtraquestionAnsDetailComponent } from './detail/extraquestion-ans-detail.component';
import { ExtraquestionAnsUpdateComponent } from './update/extraquestion-ans-update.component';
import ExtraquestionAnsResolve from './route/extraquestion-ans-routing-resolve.service';

const extraquestionAnsRoute: Routes = [
  {
    path: '',
    component: ExtraquestionAnsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExtraquestionAnsDetailComponent,
    resolve: {
      extraquestionAns: ExtraquestionAnsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExtraquestionAnsUpdateComponent,
    resolve: {
      extraquestionAns: ExtraquestionAnsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExtraquestionAnsUpdateComponent,
    resolve: {
      extraquestionAns: ExtraquestionAnsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default extraquestionAnsRoute;
