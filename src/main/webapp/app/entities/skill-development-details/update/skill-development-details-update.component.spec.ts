import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ISkillDevelopmentType } from 'app/entities/skill-development-type/skill-development-type.model';
import { SkillDevelopmentTypeService } from 'app/entities/skill-development-type/service/skill-development-type.service';
import { IFeedbackResponder } from 'app/entities/feedback-responder/feedback-responder.model';
import { FeedbackResponderService } from 'app/entities/feedback-responder/service/feedback-responder.service';
import { ISkillDevelopmentDetails } from '../skill-development-details.model';
import { SkillDevelopmentDetailsService } from '../service/skill-development-details.service';
import { SkillDevelopmentDetailsFormService } from './skill-development-details-form.service';

import { SkillDevelopmentDetailsUpdateComponent } from './skill-development-details-update.component';

describe('SkillDevelopmentDetails Management Update Component', () => {
  let comp: SkillDevelopmentDetailsUpdateComponent;
  let fixture: ComponentFixture<SkillDevelopmentDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let skillDevelopmentDetailsFormService: SkillDevelopmentDetailsFormService;
  let skillDevelopmentDetailsService: SkillDevelopmentDetailsService;
  let skillDevelopmentTypeService: SkillDevelopmentTypeService;
  let feedbackResponderService: FeedbackResponderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SkillDevelopmentDetailsUpdateComponent],
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
      .overrideTemplate(SkillDevelopmentDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SkillDevelopmentDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    skillDevelopmentDetailsFormService = TestBed.inject(SkillDevelopmentDetailsFormService);
    skillDevelopmentDetailsService = TestBed.inject(SkillDevelopmentDetailsService);
    skillDevelopmentTypeService = TestBed.inject(SkillDevelopmentTypeService);
    feedbackResponderService = TestBed.inject(FeedbackResponderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SkillDevelopmentType query and add missing value', () => {
      const skillDevelopmentDetails: ISkillDevelopmentDetails = { id: 456 };
      const skillDevelopmentType: ISkillDevelopmentType = { id: 12503 };
      skillDevelopmentDetails.skillDevelopmentType = skillDevelopmentType;

      const skillDevelopmentTypeCollection: ISkillDevelopmentType[] = [{ id: 31012 }];
      jest.spyOn(skillDevelopmentTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: skillDevelopmentTypeCollection })));
      const additionalSkillDevelopmentTypes = [skillDevelopmentType];
      const expectedCollection: ISkillDevelopmentType[] = [...additionalSkillDevelopmentTypes, ...skillDevelopmentTypeCollection];
      jest.spyOn(skillDevelopmentTypeService, 'addSkillDevelopmentTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ skillDevelopmentDetails });
      comp.ngOnInit();

      expect(skillDevelopmentTypeService.query).toHaveBeenCalled();
      expect(skillDevelopmentTypeService.addSkillDevelopmentTypeToCollectionIfMissing).toHaveBeenCalledWith(
        skillDevelopmentTypeCollection,
        ...additionalSkillDevelopmentTypes.map(expect.objectContaining),
      );
      expect(comp.skillDevelopmentTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FeedbackResponder query and add missing value', () => {
      const skillDevelopmentDetails: ISkillDevelopmentDetails = { id: 456 };
      const responder: IFeedbackResponder = { id: 18109 };
      skillDevelopmentDetails.responder = responder;

      const feedbackResponderCollection: IFeedbackResponder[] = [{ id: 23954 }];
      jest.spyOn(feedbackResponderService, 'query').mockReturnValue(of(new HttpResponse({ body: feedbackResponderCollection })));
      const additionalFeedbackResponders = [responder];
      const expectedCollection: IFeedbackResponder[] = [...additionalFeedbackResponders, ...feedbackResponderCollection];
      jest.spyOn(feedbackResponderService, 'addFeedbackResponderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ skillDevelopmentDetails });
      comp.ngOnInit();

      expect(feedbackResponderService.query).toHaveBeenCalled();
      expect(feedbackResponderService.addFeedbackResponderToCollectionIfMissing).toHaveBeenCalledWith(
        feedbackResponderCollection,
        ...additionalFeedbackResponders.map(expect.objectContaining),
      );
      expect(comp.feedbackRespondersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const skillDevelopmentDetails: ISkillDevelopmentDetails = { id: 456 };
      const skillDevelopmentType: ISkillDevelopmentType = { id: 4448 };
      skillDevelopmentDetails.skillDevelopmentType = skillDevelopmentType;
      const responder: IFeedbackResponder = { id: 5369 };
      skillDevelopmentDetails.responder = responder;

      activatedRoute.data = of({ skillDevelopmentDetails });
      comp.ngOnInit();

      expect(comp.skillDevelopmentTypesSharedCollection).toContain(skillDevelopmentType);
      expect(comp.feedbackRespondersSharedCollection).toContain(responder);
      expect(comp.skillDevelopmentDetails).toEqual(skillDevelopmentDetails);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISkillDevelopmentDetails>>();
      const skillDevelopmentDetails = { id: 123 };
      jest.spyOn(skillDevelopmentDetailsFormService, 'getSkillDevelopmentDetails').mockReturnValue(skillDevelopmentDetails);
      jest.spyOn(skillDevelopmentDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ skillDevelopmentDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: skillDevelopmentDetails }));
      saveSubject.complete();

      // THEN
      expect(skillDevelopmentDetailsFormService.getSkillDevelopmentDetails).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(skillDevelopmentDetailsService.update).toHaveBeenCalledWith(expect.objectContaining(skillDevelopmentDetails));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISkillDevelopmentDetails>>();
      const skillDevelopmentDetails = { id: 123 };
      jest.spyOn(skillDevelopmentDetailsFormService, 'getSkillDevelopmentDetails').mockReturnValue({ id: null });
      jest.spyOn(skillDevelopmentDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ skillDevelopmentDetails: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: skillDevelopmentDetails }));
      saveSubject.complete();

      // THEN
      expect(skillDevelopmentDetailsFormService.getSkillDevelopmentDetails).toHaveBeenCalled();
      expect(skillDevelopmentDetailsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISkillDevelopmentDetails>>();
      const skillDevelopmentDetails = { id: 123 };
      jest.spyOn(skillDevelopmentDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ skillDevelopmentDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(skillDevelopmentDetailsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSkillDevelopmentType', () => {
      it('Should forward to skillDevelopmentTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(skillDevelopmentTypeService, 'compareSkillDevelopmentType');
        comp.compareSkillDevelopmentType(entity, entity2);
        expect(skillDevelopmentTypeService.compareSkillDevelopmentType).toHaveBeenCalledWith(entity, entity2);
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
