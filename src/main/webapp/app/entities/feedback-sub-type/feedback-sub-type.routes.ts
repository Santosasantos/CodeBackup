import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { FeedbackSubTypeComponent } from './list/feedback-sub-type.component';
import { FeedbackSubTypeDetailComponent } from './detail/feedback-sub-type-detail.component';
import { FeedbackSubTypeUpdateComponent } from './update/feedback-sub-type-update.component';
import FeedbackSubTypeResolve from './route/feedback-sub-type-routing-resolve.service';

const feedbackSubTypeRoute: Routes = [
  {
    path: '',
    component: FeedbackSubTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FeedbackSubTypeDetailComponent,
    resolve: {
      feedbackSubType: FeedbackSubTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FeedbackSubTypeUpdateComponent,
    resolve: {
      feedbackSubType: FeedbackSubTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FeedbackSubTypeUpdateComponent,
    resolve: {
      feedbackSubType: FeedbackSubTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default feedbackSubTypeRoute;
