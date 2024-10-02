import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'epmsApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'employee',
    data: { pageTitle: 'epmsApp.employee.home.title' },
    loadChildren: () => import('./employee/employee.routes'),
  },
  {
    path: 'feedback',
    data: { pageTitle: 'epmsApp.feedback.home.title' },
    loadChildren: () => import('./feedback/feedback.routes'),
  },
  {
    path: 'feedback-responder',
    data: { pageTitle: 'epmsApp.feedbackResponder.home.title' },
    loadChildren: () => import('./feedback-responder/feedback-responder.routes'),
  },
  {
    path: 'feedback-details',
    data: { pageTitle: 'epmsApp.feedbackDetails.home.title' },
    loadChildren: () => import('./feedback-details/feedback-details.routes'),
  },
  {
    path: 'feedback-sub-type',
    data: { pageTitle: 'epmsApp.feedbackSubType.home.title' },
    loadChildren: () => import('./feedback-sub-type/feedback-sub-type.routes'),
  },
  {
    path: 'feedback-type',
    data: { pageTitle: 'epmsApp.feedbackType.home.title' },
    loadChildren: () => import('./feedback-type/feedback-type.routes'),
  },
  {
    path: 'skill-development-details',
    data: { pageTitle: 'epmsApp.skillDevelopmentDetails.home.title' },
    loadChildren: () => import('./skill-development-details/skill-development-details.routes'),
  },
  {
    path: 'skill-development-type',
    data: { pageTitle: 'epmsApp.skillDevelopmentType.home.title' },
    loadChildren: () => import('./skill-development-type/skill-development-type.routes'),
  },
  {
    path: 'teach-other',
    data: { pageTitle: 'epmsApp.teachOther.home.title' },
    loadChildren: () => import('./teach-other/teach-other.routes'),
  },
  {
    path: 'extraquestion',
    data: { pageTitle: 'epmsApp.extraquestion.home.title' },
    loadChildren: () => import('./extraquestion/extraquestion.routes'),
  },
  {
    path: 'extraquestion-ans',
    data: { pageTitle: 'epmsApp.extraquestionAns.home.title' },
    loadChildren: () => import('./extraquestion-ans/extraquestion-ans.routes'),
  },
  {
    path: 'rating-scale',
    data: { pageTitle: 'epmsApp.ratingScale.home.title' },
    loadChildren: () => import('./rating-scale/rating-scale.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
