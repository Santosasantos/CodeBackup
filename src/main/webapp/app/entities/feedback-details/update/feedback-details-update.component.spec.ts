import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IFeedbackSubType } from 'app/entities/feedback-sub-type/feedback-sub-type.model';
import { FeedbackSubTypeService } from 'app/entities/feedback-sub-type/service/feedback-sub-type.service';
import { IFeedbackResponder } from 'app/entities/feedback-responder/feedback-responder.model';
import { FeedbackResponderService } from 'app/entities/feedback-responder/service/feedback-responder.service';
import { IFeedbackDetails } from '../feedback-details.model';
import { FeedbackDetailsService } from '../service/feedback-details.service';
import { FeedbackDetailsFormService } from './feedback-details-form.service';

import { FeedbackDetailsUpdateComponent } from './feedback-details-update.component';

describe('FeedbackDetails Management Update Component', () => {
  let comp: FeedbackDetailsUpdateComponent;
  let fixture: ComponentFixture<FeedbackDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let feedbackDetailsFormService: FeedbackDetailsFormService;
  let feedbackDetailsService: FeedbackDetailsService;
  let feedbackSubTypeService: FeedbackSubTypeService;
  let feedbackResponderService: FeedbackResponderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FeedbackDetailsUpdateComponent],
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
      .overrideTemplate(FeedbackDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FeedbackDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    feedbackDetailsFormService = TestBed.inject(FeedbackDetailsFormService);
    feedbackDetailsService = TestBed.inject(FeedbackDetailsService);
    feedbackSubTypeService = TestBed.inject(FeedbackSubTypeService);
    feedbackResponderService = TestBed.inject(FeedbackResponderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call FeedbackSubType query and add missing value', () => {
      const feedbackDetails: IFeedbackDetails = { id: 456 };
      const feedbackSubType: IFeedbackSubType = { id: 27916 };
      feedbackDetails.feedbackSubType = feedbackSubType;

      const feedbackSubTypeCollection: IFeedbackSubType[] = [{ id: 28676 }];
      jest.spyOn(feedbackSubTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: feedbackSubTypeCollection })));
      const additionalFeedbackSubTypes = [feedbackSubType];
      const expectedCollection: IFeedbackSubType[] = [...additionalFeedbackSubTypes, ...feedbackSubTypeCollection];
      jest.spyOn(feedbackSubTypeService, 'addFeedbackSubTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ feedbackDetails });
      comp.ngOnInit();

      expect(feedbackSubTypeService.query).toHaveBeenCalled();
      expect(feedbackSubTypeService.addFeedbackSubTypeToCollectionIfMissing).toHaveBeenCalledWith(
        feedbackSubTypeCollection,
        ...additionalFeedbackSubTypes.map(expect.objectContaining),
      );
      expect(comp.feedbackSubTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FeedbackResponder query and add missing value', () => {
      const feedbackDetails: IFeedbackDetails = { id: 456 };
      const responder: IFeedbackResponder = { id: 5790 };
      feedbackDetails.responder = responder;

      const feedbackResponderCollection: IFeedbackResponder[] = [{ id: 15142 }];
      jest.spyOn(feedbackResponderService, 'query').mockReturnValue(of(new HttpResponse({ body: feedbackResponderCollection })));
      const additionalFeedbackResponders = [responder];
      const expectedCollection: IFeedbackResponder[] = [...additionalFeedbackResponders, ...feedbackResponderCollection];
      jest.spyOn(feedbackResponderService, 'addFeedbackResponderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ feedbackDetails });
      comp.ngOnInit();

      expect(feedbackResponderService.query).toHaveBeenCalled();
      expect(feedbackResponderService.addFeedbackResponderToCollectionIfMissing).toHaveBeenCalledWith(
        feedbackResponderCollection,
        ...additionalFeedbackResponders.map(expect.objectContaining),
      );
      expect(comp.feedbackRespondersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const feedbackDetails: IFeedbackDetails = { id: 456 };
      const feedbackSubType: IFeedbackSubType = { id: 3562 };
      feedbackDetails.feedbackSubType = feedbackSubType;
      const responder: IFeedbackResponder = { id: 7991 };
      feedbackDetails.responder = responder;

      activatedRoute.data = of({ feedbackDetails });
      comp.ngOnInit();

      expect(comp.feedbackSubTypesSharedCollection).toContain(feedbackSubType);
      expect(comp.feedbackRespondersSharedCollection).toContain(responder);
      expect(comp.feedbackDetails).toEqual(feedbackDetails);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedbackDetails>>();
      const feedbackDetails = { id: 123 };
      jest.spyOn(feedbackDetailsFormService, 'getFeedbackDetails').mockReturnValue(feedbackDetails);
      jest.spyOn(feedbackDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedbackDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feedbackDetails }));
      saveSubject.complete();

      // THEN
      expect(feedbackDetailsFormService.getFeedbackDetails).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(feedbackDetailsService.update).toHaveBeenCalledWith(expect.objectContaining(feedbackDetails));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedbackDetails>>();
      const feedbackDetails = { id: 123 };
      jest.spyOn(feedbackDetailsFormService, 'getFeedbackDetails').mockReturnValue({ id: null });
      jest.spyOn(feedbackDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedbackDetails: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feedbackDetails }));
      saveSubject.complete();

      // THEN
      expect(feedbackDetailsFormService.getFeedbackDetails).toHaveBeenCalled();
      expect(feedbackDetailsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedbackDetails>>();
      const feedbackDetails = { id: 123 };
      jest.spyOn(feedbackDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedbackDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(feedbackDetailsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFeedbackSubType', () => {
      it('Should forward to feedbackSubTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(feedbackSubTypeService, 'compareFeedbackSubType');
        comp.compareFeedbackSubType(entity, entity2);
        expect(feedbackSubTypeService.compareFeedbackSubType).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareFeedbackResponder', () => {
      it('Should forward to feedbackResponderService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(feedbackResponderService, 'compareFeedbackResponder');
        comp.compareFeedbackResponder(entity, entity2);
        expect(feedbackResponderService.compareFeedbackResponder).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
