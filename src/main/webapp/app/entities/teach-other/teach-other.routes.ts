import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TeachOtherComponent } from './list/teach-other.component';
import { TeachOtherDetailComponent } from './detail/teach-other-detail.component';
import { TeachOtherUpdateComponent } from './update/teach-other-update.component';
import TeachOtherResolve from './route/teach-other-routing-resolve.service';

const teachOtherRoute: Routes = [
  {
    path: '',
    component: TeachOtherComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TeachOtherDetailComponent,
    resolve: {
      teachOther: TeachOtherResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TeachOtherUpdateComponent,
    resolve: {
      teachOther: TeachOtherResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TeachOtherUpdateComponent,
    resolve: {
      teachOther: TeachOtherResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default teachOtherRoute;
