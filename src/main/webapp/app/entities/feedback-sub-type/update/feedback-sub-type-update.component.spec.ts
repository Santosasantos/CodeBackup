import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IFeedbackType } from 'app/entities/feedback-type/feedback-type.model';
import { FeedbackTypeService } from 'app/entities/feedback-type/service/feedback-type.service';
import { FeedbackSubTypeService } from '../service/feedback-sub-type.service';
import { IFeedbackSubType } from '../feedback-sub-type.model';
import { FeedbackSubTypeFormService } from './feedback-sub-type-form.service';

import { FeedbackSubTypeUpdateComponent } from './feedback-sub-type-update.component';

describe('FeedbackSubType Management Update Component', () => {
  let comp: FeedbackSubTypeUpdateComponent;
  let fixture: ComponentFixture<FeedbackSubTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let feedbackSubTypeFormService: FeedbackSubTypeFormService;
  let feedbackSubTypeService: FeedbackSubTypeService;
  let feedbackTypeService: FeedbackTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FeedbackSubTypeUpdateComponent],
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
      .overrideTemplate(FeedbackSubTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FeedbackSubTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    feedbackSubTypeFormService = TestBed.inject(FeedbackSubTypeFormService);
    feedbackSubTypeService = TestBed.inject(FeedbackSubTypeService);
    feedbackTypeService = TestBed.inject(FeedbackTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call FeedbackType query and add missing value', () => {
      const feedbackSubType: IFeedbackSubType = { id: 456 };
      const feedbackType: IFeedbackType = { id: 26919 };
      feedbackSubType.feedbackType = feedbackType;

      const feedbackTypeCollection: IFeedbackType[] = [{ id: 16527 }];
      jest.spyOn(feedbackTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: feedbackTypeCollection })));
      const additionalFeedbackTypes = [feedbackType];
      const expectedCollection: IFeedbackType[] = [...additionalFeedbackTypes, ...feedbackTypeCollection];
      jest.spyOn(feedbackTypeService, 'addFeedbackTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ feedbackSubType });
      comp.ngOnInit();

      expect(feedbackTypeService.query).toHaveBeenCalled();
      expect(feedbackTypeService.addFeedbackTypeToCollectionIfMissing).toHaveBeenCalledWith(
        feedbackTypeCollection,
        ...additionalFeedbackTypes.map(expect.objectContaining),
      );
      expect(comp.feedbackTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const feedbackSubType: IFeedbackSubType = { id: 456 };
      const feedbackType: IFeedbackType = { id: 20526 };
      feedbackSubType.feedbackType = feedbackType;

      activatedRoute.data = of({ feedbackSubType });
      comp.ngOnInit();

      expect(comp.feedbackTypesSharedCollection).toContain(feedbackType);
      expect(comp.feedbackSubType).toEqual(feedbackSubType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedbackSubType>>();
      const feedbackSubType = { id: 123 };
      jest.spyOn(feedbackSubTypeFormService, 'getFeedbackSubType').mockReturnValue(feedbackSubType);
      jest.spyOn(feedbackSubTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedbackSubType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feedbackSubType }));
      saveSubject.complete();

      // THEN
      expect(feedbackSubTypeFormService.getFeedbackSubType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(feedbackSubTypeService.update).toHaveBeenCalledWith(expect.objectContaining(feedbackSubType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedbackSubType>>();
      const feedbackSubType = { id: 123 };
      jest.spyOn(feedbackSubTypeFormService, 'getFeedbackSubType').mockReturnValue({ id: null });
      jest.spyOn(feedbackSubTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedbackSubType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feedbackSubType }));
      saveSubject.complete();

      // THEN
      expect(feedbackSubTypeFormService.getFeedbackSubType).toHaveBeenCalled();
      expect(feedbackSubTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedbackSubType>>();
      const feedbackSubType = { id: 123 };
      jest.spyOn(feedbackSubTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedbackSubType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(feedbackSubTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFeedbackType', () => {
      it('Should forward to feedbackTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(feedbackTypeService, 'compareFeedbackType');
        comp.compareFeedbackType(entity, entity2);
        expect(feedbackTypeService.compareFeedbackType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
