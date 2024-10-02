import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IExtraquestion } from 'app/entities/extraquestion/extraquestion.model';
import { ExtraquestionService } from 'app/entities/extraquestion/service/extraquestion.service';
import { ExtraquestionAnsService } from '../service/extraquestion-ans.service';
import { IExtraquestionAns } from '../extraquestion-ans.model';
import { ExtraquestionAnsFormService } from './extraquestion-ans-form.service';

import { ExtraquestionAnsUpdateComponent } from './extraquestion-ans-update.component';

describe('ExtraquestionAns Management Update Component', () => {
  let comp: ExtraquestionAnsUpdateComponent;
  let fixture: ComponentFixture<ExtraquestionAnsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let extraquestionAnsFormService: ExtraquestionAnsFormService;
  let extraquestionAnsService: ExtraquestionAnsService;
  let extraquestionService: ExtraquestionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ExtraquestionAnsUpdateComponent],
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
      .overrideTemplate(ExtraquestionAnsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExtraquestionAnsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    extraquestionAnsFormService = TestBed.inject(ExtraquestionAnsFormService);
    extraquestionAnsService = TestBed.inject(ExtraquestionAnsService);
    extraquestionService = TestBed.inject(ExtraquestionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Extraquestion query and add missing value', () => {
      const extraquestionAns: IExtraquestionAns = { id: 456 };
      const questions: IExtraquestion = { id: 2819 };
      extraquestionAns.questions = questions;

      const extraquestionCollection: IExtraquestion[] = [{ id: 7229 }];
      jest.spyOn(extraquestionService, 'query').mockReturnValue(of(new HttpResponse({ body: extraquestionCollection })));
      const additionalExtraquestions = [questions];
      const expectedCollection: IExtraquestion[] = [...additionalExtraquestions, ...extraquestionCollection];
      jest.spyOn(extraquestionService, 'addExtraquestionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ extraquestionAns });
      comp.ngOnInit();

      expect(extraquestionService.query).toHaveBeenCalled();
      expect(extraquestionService.addExtraquestionToCollectionIfMissing).toHaveBeenCalledWith(
        extraquestionCollection,
        ...additionalExtraquestions.map(expect.objectContaining),
      );
      expect(comp.extraquestionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const extraquestionAns: IExtraquestionAns = { id: 456 };
      const questions: IExtraquestion = { id: 14725 };
      extraquestionAns.questions = questions;

      activatedRoute.data = of({ extraquestionAns });
      comp.ngOnInit();

      expect(comp.extraquestionsSharedCollection).toContain(questions);
      expect(comp.extraquestionAns).toEqual(extraquestionAns);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExtraquestionAns>>();
      const extraquestionAns = { id: 123 };
      jest.spyOn(extraquestionAnsFormService, 'getExtraquestionAns').mockReturnValue(extraquestionAns);
      jest.spyOn(extraquestionAnsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ extraquestionAns });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: extraquestionAns }));
      saveSubject.complete();

      // THEN
      expect(extraquestionAnsFormService.getExtraquestionAns).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(extraquestionAnsService.update).toHaveBeenCalledWith(expect.objectContaining(extraquestionAns));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExtraquestionAns>>();
      const extraquestionAns = { id: 123 };
      jest.spyOn(extraquestionAnsFormService, 'getExtraquestionAns').mockReturnValue({ id: null });
      jest.spyOn(extraquestionAnsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ extraquestionAns: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: extraquestionAns }));
      saveSubject.complete();

      // THEN
      expect(extraquestionAnsFormService.getExtraquestionAns).toHaveBeenCalled();
      expect(extraquestionAnsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExtraquestionAns>>();
      const extraquestionAns = { id: 123 };
      jest.spyOn(extraquestionAnsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ extraquestionAns });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(extraquestionAnsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareExtraquestion', () => {
      it('Should forward to extraquestionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(extraquestionService, 'compareExtraquestion');
        comp.compareExtraquestion(entity, entity2);
        expect(extraquestionService.compareExtraquestion).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
