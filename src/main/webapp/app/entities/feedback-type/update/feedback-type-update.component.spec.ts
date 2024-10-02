import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { FeedbackTypeService } from '../service/feedback-type.service';
import { IFeedbackType } from '../feedback-type.model';
import { FeedbackTypeFormService } from './feedback-type-form.service';

import { FeedbackTypeUpdateComponent } from './feedback-type-update.component';

describe('FeedbackType Management Update Component', () => {
  let comp: FeedbackTypeUpdateComponent;
  let fixture: ComponentFixture<FeedbackTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let feedbackTypeFormService: FeedbackTypeFormService;
  let feedbackTypeService: FeedbackTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FeedbackTypeUpdateComponent],
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
      .overrideTemplate(FeedbackTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FeedbackTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    feedbackTypeFormService = TestBed.inject(FeedbackTypeFormService);
    feedbackTypeService = TestBed.inject(FeedbackTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const feedbackType: IFeedbackType = { id: 456 };

      activatedRoute.data = of({ feedbackType });
      comp.ngOnInit();

      expect(comp.feedbackType).toEqual(feedbackType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedbackType>>();
      const feedbackType = { id: 123 };
      jest.spyOn(feedbackTypeFormService, 'getFeedbackType').mockReturnValue(feedbackType);
      jest.spyOn(feedbackTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedbackType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feedbackType }));
      saveSubject.complete();

      // THEN
      expect(feedbackTypeFormService.getFeedbackType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(feedbackTypeService.update).toHaveBeenCalledWith(expect.objectContaining(feedbackType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedbackType>>();
      const feedbackType = { id: 123 };
      jest.spyOn(feedbackTypeFormService, 'getFeedbackType').mockReturnValue({ id: null });
      jest.spyOn(feedbackTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedbackType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feedbackType }));
      saveSubject.complete();

      // THEN
      expect(feedbackTypeFormService.getFeedbackType).toHaveBeenCalled();
      expect(feedbackTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedbackType>>();
      const feedbackType = { id: 123 };
      jest.spyOn(feedbackTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedbackType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(feedbackTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
