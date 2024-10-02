import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { FeedbackDetailsDetailComponent } from './feedback-details-detail.component';

describe('FeedbackDetails Management Detail Component', () => {
  let comp: FeedbackDetailsDetailComponent;
  let fixture: ComponentFixture<FeedbackDetailsDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FeedbackDetailsDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: FeedbackDetailsDetailComponent,
              resolve: { feedbackDetails: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FeedbackDetailsDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FeedbackDetailsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load feedbackDetails on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FeedbackDetailsDetailComponent);

      // THEN
      expect(instance.feedbackDetails()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
