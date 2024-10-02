import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ExtraquestionDetailComponent } from './extraquestion-detail.component';

describe('Extraquestion Management Detail Component', () => {
  let comp: ExtraquestionDetailComponent;
  let fixture: ComponentFixture<ExtraquestionDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExtraquestionDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ExtraquestionDetailComponent,
              resolve: { extraquestion: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ExtraquestionDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExtraquestionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load extraquestion on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ExtraquestionDetailComponent);

      // THEN
      expect(instance.extraquestion()).toEqual(expect.objectContaining({ id: 123 }));
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
