import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IFeedback } from 'app/entities/feedback/feedback.model';
import { FeedbackService } from 'app/entities/feedback/service/feedback.service';
import { IFeedbackResponder } from '../feedback-responder.model';
import { FeedbackResponderService } from '../service/feedback-responder.service';
import { FeedbackResponderFormService } from './feedback-responder-form.service';

import { FeedbackResponderUpdateComponent } from './feedback-responder-update.component';

describe('FeedbackResponder Management Update Component', () => {
  let comp: FeedbackResponderUpdateComponent;
  let fixture: ComponentFixture<FeedbackResponderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let feedbackResponderFormService: FeedbackResponderFormService;
  let feedbackResponderService: FeedbackResponderService;
  let employeeService: EmployeeService;
  let feedbackService: FeedbackService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FeedbackResponderUpdateComponent],
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
      .overrideTemplate(FeedbackResponderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FeedbackResponderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    feedbackResponderFormService = TestBed.inject(FeedbackResponderFormService);
    feedbackResponderService = TestBed.inject(FeedbackResponderService);
    employeeService = TestBed.inject(EmployeeService);
    feedbackService = TestBed.inject(FeedbackService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const feedbackResponder: IFeedbackResponder = { id: 456 };
      const employee: IEmployee = { id: 9117 };
      feedbackResponder.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 27596 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ feedbackResponder });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining),
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Feedback query and add missing value', () => {
      const feedbackResponder: IFeedbackResponder = { id: 456 };
      const feedback: IFeedback = { id: 12320 };
      feedbackResponder.feedback = feedback;

      const feedbackCollection: IFeedback[] = [{ id: 25859 }];
      jest.spyOn(feedbackService, 'query').mockReturnValue(of(new HttpResponse({ body: feedbackCollection })));
      const additionalFeedbacks = [feedback];
      const expectedCollection: IFeedback[] = [...additionalFeedbacks, ...feedbackCollection];
      jest.spyOn(feedbackService, 'addFeedbackToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ feedbackResponder });
      comp.ngOnInit();

      expect(feedbackService.query).toHaveBeenCalled();
      expect(feedbackService.addFeedbackToCollectionIfMissing).toHaveBeenCalledWith(
        feedbackCollection,
        ...additionalFeedbacks.map(expect.objectContaining),
      );
      expect(comp.feedbacksSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const feedbackResponder: IFeedbackResponder = { id: 456 };
      const employee: IEmployee = { id: 16504 };
      feedbackResponder.employee = employee;
      const feedback: IFeedback = { id: 29993 };
      feedbackResponder.feedback = feedback;

      activatedRoute.data = of({ feedbackResponder });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.feedbacksSharedCollection).toContain(feedback);
      expect(comp.feedbackResponder).toEqual(feedbackResponder);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedbackResponder>>();
      const feedbackResponder = { id: 123 };
      jest.spyOn(feedbackResponderFormService, 'getFeedbackResponder').mockReturnValue(feedbackResponder);
      jest.spyOn(feedbackResponderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedbackResponder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feedbackResponder }));
      saveSubject.complete();

      // THEN
      expect(feedbackResponderFormService.getFeedbackResponder).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(feedbackResponderService.update).toHaveBeenCalledWith(expect.objectContaining(feedbackResponder));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedbackResponder>>();
      const feedbackResponder = { id: 123 };
      jest.spyOn(feedbackResponderFormService, 'getFeedbackResponder').mockReturnValue({ id: null });
      jest.spyOn(feedbackResponderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedbackResponder: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feedbackResponder }));
      saveSubject.complete();

      // THEN
      expect(feedbackResponderFormService.getFeedbackResponder).toHaveBeenCalled();
      expect(feedbackResponderService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedbackResponder>>();
      const feedbackResponder = { id: 123 };
      jest.spyOn(feedbackResponderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedbackResponder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(feedbackResponderService.update).toHaveBeenCalled();
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
