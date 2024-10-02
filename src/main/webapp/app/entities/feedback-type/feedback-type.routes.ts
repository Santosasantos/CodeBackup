import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { FeedbackTypeComponent } from './list/feedback-type.component';
import { FeedbackTypeDetailComponent } from './detail/feedback-type-detail.component';
import { FeedbackTypeUpdateComponent } from './update/feedback-type-update.component';
import FeedbackTypeResolve from './route/feedback-type-routing-resolve.service';

const feedbackTypeRoute: Routes = [
  {
    path: '',
    component: FeedbackTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FeedbackTypeDetailComponent,
    resolve: {
      feedbackType: FeedbackTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FeedbackTypeUpdateComponent,
    resolve: {
      feedbackType: FeedbackTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FeedbackTypeUpdateComponent,
    resolve: {
      feedbackType: FeedbackTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default feedbackTypeRoute;
