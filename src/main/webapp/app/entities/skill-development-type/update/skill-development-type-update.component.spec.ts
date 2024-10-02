import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { SkillDevelopmentTypeService } from '../service/skill-development-type.service';
import { ISkillDevelopmentType } from '../skill-development-type.model';
import { SkillDevelopmentTypeFormService } from './skill-development-type-form.service';

import { SkillDevelopmentTypeUpdateComponent } from './skill-development-type-update.component';

describe('SkillDevelopmentType Management Update Component', () => {
  let comp: SkillDevelopmentTypeUpdateComponent;
  let fixture: ComponentFixture<SkillDevelopmentTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let skillDevelopmentTypeFormService: SkillDevelopmentTypeFormService;
  let skillDevelopmentTypeService: SkillDevelopmentTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SkillDevelopmentTypeUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SkillDevelopmentTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SkillDevelopmentTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    skillDevelopmentTypeFormService = TestBed.inject(SkillDevelopmentTypeFormService);
    skillDevelopmentTypeService = TestBed.inject(SkillDevelopmentTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const skillDevelopmentType: ISkillDevelopmentType = { id: 456 };

      activatedRoute.data = of({ skillDevelopmentType });
      comp.ngOnInit();

      expect(comp.skillDevelopmentType).toEqual(skillDevelopmentType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISkillDevelopmentType>>();
      const skillDevelopmentType = { id: 123 };
      jest.spyOn(skillDevelopmentTypeFormService, 'getSkillDevelopmentType').mockReturnValue(skillDevelopmentType);
      jest.spyOn(skillDevelopmentTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ skillDevelopmentType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: skillDevelopmentType }));
      saveSubject.complete();

      // THEN
      expect(skillDevelopmentTypeFormService.getSkillDevelopmentType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(skillDevelopmentTypeService.update).toHaveBeenCalledWith(expect.objectContaining(skillDevelopmentType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISkillDevelopmentType>>();
      const skillDevelopmentType = { id: 123 };
      jest.spyOn(skillDevelopmentTypeFormService, 'getSkillDevelopmentType').mockReturnValue({ id: null });
      jest.spyOn(skillDevelopmentTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ skillDevelopmentType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: skillDevelopmentType }));
      saveSubject.complete();

      // THEN
      expect(skillDevelopmentTypeFormService.getSkillDevelopmentType).toHaveBeenCalled();
      expect(skillDevelopmentTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISkillDevelopmentType>>();
      const skillDevelopmentType = { id: 123 };
      jest.spyOn(skillDevelopmentTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ skillDevelopmentType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(skillDevelopmentTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
