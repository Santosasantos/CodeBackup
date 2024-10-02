import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { FeedbackResponderDetailComponent } from './feedback-responder-detail.component';

describe('FeedbackResponder Management Detail Component', () => {
  let comp: FeedbackResponderDetailComponent;
  let fixture: ComponentFixture<FeedbackResponderDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FeedbackResponderDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: FeedbackResponderDetailComponent,
              resolve: { feedbackResponder: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FeedbackResponderDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FeedbackResponderDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load feedbackResponder on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FeedbackResponderDetailComponent);

      // THEN
      expect(instance.feedbackResponder()).toEqual(expect.objectContaining({ id: 123 }));
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
