import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { FeedbackSubTypeDetailComponent } from './feedback-sub-type-detail.component';

describe('FeedbackSubType Management Detail Component', () => {
  let comp: FeedbackSubTypeDetailComponent;
  let fixture: ComponentFixture<FeedbackSubTypeDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FeedbackSubTypeDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: FeedbackSubTypeDetailComponent,
              resolve: { feedbackSubType: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FeedbackSubTypeDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FeedbackSubTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load feedbackSubType on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FeedbackSubTypeDetailComponent);

      // THEN
      expect(instance.feedbackSubType()).toEqual(expect.objectContaining({ id: 123 }));
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
