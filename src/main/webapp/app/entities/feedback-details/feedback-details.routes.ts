import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { FeedbackDetailsComponent } from './list/feedback-details.component';
import { FeedbackDetailsDetailComponent } from './detail/feedback-details-detail.component';
import { FeedbackDetailsUpdateComponent } from './update/feedback-details-update.component';
import FeedbackDetailsResolve from './route/feedback-details-routing-resolve.service';

const feedbackDetailsRoute: Routes = [
  {
    path: '',
    component: FeedbackDetailsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FeedbackDetailsDetailComponent,
    resolve: {
      feedbackDetails: FeedbackDetailsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FeedbackDetailsUpdateComponent,
    resolve: {
      feedbackDetails: FeedbackDetailsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FeedbackDetailsUpdateComponent,
    resolve: {
      feedbackDetails: FeedbackDetailsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default feedbackDetailsRoute;
