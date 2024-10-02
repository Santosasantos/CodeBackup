import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ExtraquestionComponent } from './list/extraquestion.component';
import { ExtraquestionDetailComponent } from './detail/extraquestion-detail.component';
import { ExtraquestionUpdateComponent } from './update/extraquestion-update.component';
import ExtraquestionResolve from './route/extraquestion-routing-resolve.service';

const extraquestionRoute: Routes = [
  {
    path: '',
    component: ExtraquestionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExtraquestionDetailComponent,
    resolve: {
      extraquestion: ExtraquestionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExtraquestionUpdateComponent,
    resolve: {
      extraquestion: ExtraquestionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExtraquestionUpdateComponent,
    resolve: {
      extraquestion: ExtraquestionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default extraquestionRoute;
