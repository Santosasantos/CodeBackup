import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TeachOtherDetailComponent } from './teach-other-detail.component';

describe('TeachOther Management Detail Component', () => {
  let comp: TeachOtherDetailComponent;
  let fixture: ComponentFixture<TeachOtherDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TeachOtherDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TeachOtherDetailComponent,
              resolve: { teachOther: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TeachOtherDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TeachOtherDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load teachOther on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TeachOtherDetailComponent);

      // THEN
      expect(instance.teachOther()).toEqual(expect.objectContaining({ id: 123 }));
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
