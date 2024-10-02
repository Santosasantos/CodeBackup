import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { FeedbackComponent } from './list/feedback.component';
import { FeedbackDetailComponent } from './detail/feedback-detail.component';
import { FeedbackUpdateComponent } from './update/feedback-update.component';
import FeedbackResolve from './route/feedback-routing-resolve.service';

const feedbackRoute: Routes = [
  {
    path: '',
    component: FeedbackComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FeedbackDetailComponent,
    resolve: {
      feedback: FeedbackResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FeedbackUpdateComponent,
    resolve: {
      feedback: FeedbackResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FeedbackUpdateComponent,
    resolve: {
      feedback: FeedbackResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default feedbackRoute;
