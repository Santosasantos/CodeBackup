import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IFeedbackResponder } from 'app/entities/feedback-responder/feedback-responder.model';
import { FeedbackResponderService } from 'app/entities/feedback-responder/service/feedback-responder.service';
import { TeachOtherService } from '../service/teach-other.service';
import { ITeachOther } from '../teach-other.model';
import { TeachOtherFormService } from './teach-other-form.service';

import { TeachOtherUpdateComponent } from './teach-other-update.component';

describe('TeachOther Management Update Component', () => {
  let comp: TeachOtherUpdateComponent;
  let fixture: ComponentFixture<TeachOtherUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let teachOtherFormService: TeachOtherFormService;
  let teachOtherService: TeachOtherService;
  let feedbackResponderService: FeedbackResponderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TeachOtherUpdateComponent],
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
      .overrideTemplate(TeachOtherUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TeachOtherUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    teachOtherFormService = TestBed.inject(TeachOtherFormService);
    teachOtherService = TestBed.inject(TeachOtherService);
    feedbackResponderService = TestBed.inject(FeedbackResponderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call FeedbackResponder query and add missing value', () => {
      const teachOther: ITeachOther = { id: 456 };
      const responder: IFeedbackResponder = { id: 8926 };
      teachOther.responder = responder;

      const feedbackResponderCollection: IFeedbackResponder[] = [{ id: 408 }];
      jest.spyOn(feedbackResponderService, 'query').mockReturnValue(of(new HttpResponse({ body: feedbackResponderCollection })));
      const additionalFeedbackResponders = [responder];
      const expectedCollection: IFeedbackResponder[] = [...additionalFeedbackResponders, ...feedbackResponderCollection];
      jest.spyOn(feedbackResponderService, 'addFeedbackResponderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ teachOther });
      comp.ngOnInit();

      expect(feedbackResponderService.query).toHaveBeenCalled();
      expect(feedbackResponderService.addFeedbackResponderToCollectionIfMissing).toHaveBeenCalledWith(
        feedbackResponderCollection,
        ...additionalFeedbackResponders.map(expect.objectContaining),
      );
      expect(comp.feedbackRespondersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const teachOther: ITeachOther = { id: 456 };
      const responder: IFeedbackResponder = { id: 16051 };
      teachOther.responder = responder;

      activatedRoute.data = of({ teachOther });
      comp.ngOnInit();

      expect(comp.feedbackRespondersSharedCollection).toContain(responder);
      expect(comp.teachOther).toEqual(teachOther);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITeachOther>>();
      const teachOther = { id: 123 };
      jest.spyOn(teachOtherFormService, 'getTeachOther').mockReturnValue(teachOther);
      jest.spyOn(teachOtherService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ teachOther });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: teachOther }));
      saveSubject.complete();

      // THEN
      expect(teachOtherFormService.getTeachOther).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(teachOtherService.update).toHaveBeenCalledWith(expect.objectContaining(teachOther));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITeachOther>>();
      const teachOther = { id: 123 };
      jest.spyOn(teachOtherFormService, 'getTeachOther').mockReturnValue({ id: null });
      jest.spyOn(teachOtherService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ teachOther: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: teachOther }));
      saveSubject.complete();

      // THEN
      expect(teachOtherFormService.getTeachOther).toHaveBeenCalled();
      expect(teachOtherService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITeachOther>>();
      const teachOther = { id: 123 };
      jest.spyOn(teachOtherService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ teachOther });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(teachOtherService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
