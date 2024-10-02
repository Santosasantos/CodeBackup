import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SkillDevelopmentDetailsDetailComponent } from './skill-development-details-detail.component';

describe('SkillDevelopmentDetails Management Detail Component', () => {
  let comp: SkillDevelopmentDetailsDetailComponent;
  let fixture: ComponentFixture<SkillDevelopmentDetailsDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SkillDevelopmentDetailsDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: SkillDevelopmentDetailsDetailComponent,
              resolve: { skillDevelopmentDetails: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SkillDevelopmentDetailsDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SkillDevelopmentDetailsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load skillDevelopmentDetails on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SkillDevelopmentDetailsDetailComponent);

      // THEN
      expect(instance.skillDevelopmentDetails()).toEqual(expect.objectContaining({ id: 123 }));
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
