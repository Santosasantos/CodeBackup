import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SkillDevelopmentTypeDetailComponent } from './skill-development-type-detail.component';

describe('SkillDevelopmentType Management Detail Component', () => {
  let comp: SkillDevelopmentTypeDetailComponent;
  let fixture: ComponentFixture<SkillDevelopmentTypeDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SkillDevelopmentTypeDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: SkillDevelopmentTypeDetailComponent,
              resolve: { skillDevelopmentType: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SkillDevelopmentTypeDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SkillDevelopmentTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load skillDevelopmentType on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SkillDevelopmentTypeDetailComponent);

      // THEN
      expect(instance.skillDevelopmentType()).toEqual(expect.objectContaining({ id: 123 }));
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
