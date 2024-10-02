import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IFeedback } from 'app/entities/feedback/feedback.model';
import { FeedbackService } from 'app/entities/feedback/service/feedback.service';
import { ExtraquestionService } from '../service/extraquestion.service';
import { IExtraquestion } from '../extraquestion.model';
import { ExtraquestionFormService } from './extraquestion-form.service';

import { ExtraquestionUpdateComponent } from './extraquestion-update.component';

describe('Extraquestion Management Update Component', () => {
  let comp: ExtraquestionUpdateComponent;
  let fixture: ComponentFixture<ExtraquestionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let extraquestionFormService: ExtraquestionFormService;
  let extraquestionService: ExtraquestionService;
  let feedbackService: FeedbackService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ExtraquestionUpdateComponent],
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
      .overrideTemplate(ExtraquestionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExtraquestionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    extraquestionFormService = TestBed.inject(ExtraquestionFormService);
    extraquestionService = TestBed.inject(ExtraquestionService);
    feedbackService = TestBed.inject(FeedbackService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Feedback query and add missing value', () => {
      const extraquestion: IExtraquestion = { id: 456 };
      const feedback: IFeedback = { id: 27494 };
      extraquestion.feedback = feedback;

      const feedbackCollection: IFeedback[] = [{ id: 27625 }];
      jest.spyOn(feedbackService, 'query').mockReturnValue(of(new HttpResponse({ body: feedbackCollection })));
      const additionalFeedbacks = [feedback];
      const expectedCollection: IFeedback[] = [...additionalFeedbacks, ...feedbackCollection];
      jest.spyOn(feedbackService, 'addFeedbackToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ extraquestion });
      comp.ngOnInit();

      expect(feedbackService.query).toHaveBeenCalled();
      expect(feedbackService.addFeedbackToCollectionIfMissing).toHaveBeenCalledWith(
        feedbackCollection,
        ...additionalFeedbacks.map(expect.objectContaining),
      );
      expect(comp.feedbacksSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const extraquestion: IExtraquestion = { id: 456 };
      const feedback: IFeedback = { id: 7445 };
      extraquestion.feedback = feedback;

      activatedRoute.data = of({ extraquestion });
      comp.ngOnInit();

      expect(comp.feedbacksSharedCollection).toContain(feedback);
      expect(comp.extraquestion).toEqual(extraquestion);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExtraquestion>>();
      const extraquestion = { id: 123 };
      jest.spyOn(extraquestionFormService, 'getExtraquestion').mockReturnValue(extraquestion);
      jest.spyOn(extraquestionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ extraquestion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: extraquestion }));
      saveSubject.complete();

      // THEN
      expect(extraquestionFormService.getExtraquestion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(extraquestionService.update).toHaveBeenCalledWith(expect.objectContaining(extraquestion));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExtraquestion>>();
      const extraquestion = { id: 123 };
      jest.spyOn(extraquestionFormService, 'getExtraquestion').mockReturnValue({ id: null });
      jest.spyOn(extraquestionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ extraquestion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: extraquestion }));
      saveSubject.complete();

      // THEN
      expect(extraquestionFormService.getExtraquestion).toHaveBeenCalled();
      expect(extraquestionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExtraquestion>>();
      const extraquestion = { id: 123 };
      jest.spyOn(extraquestionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ extraquestion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(extraquestionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFeedback', () => {
      it('Should forward to feedbackService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(feedbackService, 'compareFeedback');
        comp.compareFeedback(entity, entity2);
        expect(feedbackService.compareFeedback).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
