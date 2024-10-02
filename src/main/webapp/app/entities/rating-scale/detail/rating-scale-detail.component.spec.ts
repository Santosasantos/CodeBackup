import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { RatingScaleDetailComponent } from './rating-scale-detail.component';

describe('RatingScale Management Detail Component', () => {
  let comp: RatingScaleDetailComponent;
  let fixture: ComponentFixture<RatingScaleDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RatingScaleDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: RatingScaleDetailComponent,
              resolve: { ratingScale: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(RatingScaleDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RatingScaleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load ratingScale on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', RatingScaleDetailComponent);

      // THEN
      expect(instance.ratingScale()).toEqual(expect.objectContaining({ id: 123 }));
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
