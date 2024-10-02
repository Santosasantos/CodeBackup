import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { RatingScaleComponent } from './list/rating-scale.component';
import { RatingScaleDetailComponent } from './detail/rating-scale-detail.component';
import { RatingScaleUpdateComponent } from './update/rating-scale-update.component';
import RatingScaleResolve from './route/rating-scale-routing-resolve.service';

const ratingScaleRoute: Routes = [
  {
    path: '',
    component: RatingScaleComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RatingScaleDetailComponent,
    resolve: {
      ratingScale: RatingScaleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RatingScaleUpdateComponent,
    resolve: {
      ratingScale: RatingScaleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RatingScaleUpdateComponent,
    resolve: {
      ratingScale: RatingScaleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ratingScaleRoute;
