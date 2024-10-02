import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ExtraquestionAnsDetailComponent } from './extraquestion-ans-detail.component';

describe('ExtraquestionAns Management Detail Component', () => {
  let comp: ExtraquestionAnsDetailComponent;
  let fixture: ComponentFixture<ExtraquestionAnsDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExtraquestionAnsDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ExtraquestionAnsDetailComponent,
              resolve: { extraquestionAns: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ExtraquestionAnsDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExtraquestionAnsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load extraquestionAns on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ExtraquestionAnsDetailComponent);

      // THEN
      expect(instance.extraquestionAns()).toEqual(expect.objectContaining({ id: 123 }));
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
