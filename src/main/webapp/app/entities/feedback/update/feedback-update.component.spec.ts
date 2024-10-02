import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IRatingScale } from 'app/entities/rating-scale/rating-scale.model';
import { RatingScaleService } from 'app/entities/rating-scale/service/rating-scale.service';
import { IFeedback } from '../feedback.model';
import { FeedbackService } from '../service/feedback.service';
import { FeedbackFormService } from './feedback-form.service';

import { FeedbackUpdateComponent } from './feedback-update.component';

describe('Feedback Management Update Component', () => {
  let comp: FeedbackUpdateComponent;
  let fixture: ComponentFixture<FeedbackUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let feedbackFormService: FeedbackFormService;
  let feedbackService: FeedbackService;
  let employeeService: EmployeeService;
  let ratingScaleService: RatingScaleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FeedbackUpdateComponent],
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
      .overrideTemplate(FeedbackUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FeedbackUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    feedbackFormService = TestBed.inject(FeedbackFormService);
    feedbackService = TestBed.inject(FeedbackService);
    employeeService = TestBed.inject(EmployeeService);
    ratingScaleService = TestBed.inject(RatingScaleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const feedback: IFeedback = { id: 456 };
      const requester: IEmployee = { id: 28159 };
      feedback.requester = requester;

      const employeeCollection: IEmployee[] = [{ id: 17346 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [requester];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ feedback });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining),
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call RatingScale query and add missing value', () => {
      const feedback: IFeedback = { id: 456 };
      const ratingScale: IRatingScale = { id: 32073 };
      feedback.ratingScale = ratingScale;

      const ratingScaleCollection: IRatingScale[] = [{ id: 19148 }];
      jest.spyOn(ratingScaleService, 'query').mockReturnValue(of(new HttpResponse({ body: ratingScaleCollection })));
      const additionalRatingScales = [ratingScale];
      const expectedCollection: IRatingScale[] = [...additionalRatingScales, ...ratingScaleCollection];
      jest.spyOn(ratingScaleService, 'addRatingScaleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ feedback });
      comp.ngOnInit();

      expect(ratingScaleService.query).toHaveBeenCalled();
      expect(ratingScaleService.addRatingScaleToCollectionIfMissing).toHaveBeenCalledWith(
        ratingScaleCollection,
        ...additionalRatingScales.map(expect.objectContaining),
      );
      expect(comp.ratingScalesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const feedback: IFeedback = { id: 456 };
      const requester: IEmployee = { id: 29224 };
      feedback.requester = requester;
      const ratingScale: IRatingScale = { id: 5338 };
      feedback.ratingScale = ratingScale;

      activatedRoute.data = of({ feedback });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(requester);
      expect(comp.ratingScalesSharedCollection).toContain(ratingScale);
      expect(comp.feedback).toEqual(feedback);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedback>>();
      const feedback = { id: 123 };
      jest.spyOn(feedbackFormService, 'getFeedback').mockReturnValue(feedback);
      jest.spyOn(feedbackService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedback });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feedback }));
      saveSubject.complete();

      // THEN
      expect(feedbackFormService.getFeedback).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(feedbackService.update).toHaveBeenCalledWith(expect.objectContaining(feedback));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedback>>();
      const feedback = { id: 123 };
      jest.spyOn(feedbackFormService, 'getFeedback').mockReturnValue({ id: null });
      jest.spyOn(feedbackService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedback: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feedback }));
      saveSubject.complete();

      // THEN
      expect(feedbackFormService.getFeedback).toHaveBeenCalled();
      expect(feedbackService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedback>>();
      const feedback = { id: 123 };
      jest.spyOn(feedbackService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedback });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(feedbackService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEmployee', () => {
      it('Should forward to employeeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employeeService, 'compareEmployee');
        comp.compareEmployee(entity, entity2);
        expect(employeeService.compareEmployee).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareRatingScale', () => {
      it('Should forward to ratingScaleService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(ratingScaleService, 'compareRatingScale');
        comp.compareRatingScale(entity, entity2);
        expect(ratingScaleService.compareRatingScale).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
