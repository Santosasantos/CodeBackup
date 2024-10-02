import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { FeedbackResponderComponent } from './list/feedback-responder.component';
import { FeedbackResponderDetailComponent } from './detail/feedback-responder-detail.component';
import { FeedbackResponderUpdateComponent } from './update/feedback-responder-update.component';
import FeedbackResponderResolve from './route/feedback-responder-routing-resolve.service';

const feedbackResponderRoute: Routes = [
  {
    path: '',
    component: FeedbackResponderComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FeedbackResponderDetailComponent,
    resolve: {
      feedbackResponder: FeedbackResponderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FeedbackResponderUpdateComponent,
    resolve: {
      feedbackResponder: FeedbackResponderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FeedbackResponderUpdateComponent,
    resolve: {
      feedbackResponder: FeedbackResponderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default feedbackResponderRoute;
