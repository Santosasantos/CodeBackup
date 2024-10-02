import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { RatingScaleService } from '../service/rating-scale.service';
import { IRatingScale } from '../rating-scale.model';
import { RatingScaleFormService } from './rating-scale-form.service';

import { RatingScaleUpdateComponent } from './rating-scale-update.component';

describe('RatingScale Management Update Component', () => {
  let comp: RatingScaleUpdateComponent;
  let fixture: ComponentFixture<RatingScaleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ratingScaleFormService: RatingScaleFormService;
  let ratingScaleService: RatingScaleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RatingScaleUpdateComponent],
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
      .overrideTemplate(RatingScaleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RatingScaleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ratingScaleFormService = TestBed.inject(RatingScaleFormService);
    ratingScaleService = TestBed.inject(RatingScaleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const ratingScale: IRatingScale = { id: 456 };

      activatedRoute.data = of({ ratingScale });
      comp.ngOnInit();

      expect(comp.ratingScale).toEqual(ratingScale);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRatingScale>>();
      const ratingScale = { id: 123 };
      jest.spyOn(ratingScaleFormService, 'getRatingScale').mockReturnValue(ratingScale);
      jest.spyOn(ratingScaleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ratingScale });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ratingScale }));
      saveSubject.complete();

      // THEN
      expect(ratingScaleFormService.getRatingScale).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ratingScaleService.update).toHaveBeenCalledWith(expect.objectContaining(ratingScale));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRatingScale>>();
      const ratingScale = { id: 123 };
      jest.spyOn(ratingScaleFormService, 'getRatingScale').mockReturnValue({ id: null });
      jest.spyOn(ratingScaleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ratingScale: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ratingScale }));
      saveSubject.complete();

      // THEN
      expect(ratingScaleFormService.getRatingScale).toHaveBeenCalled();
      expect(ratingScaleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRatingScale>>();
      const ratingScale = { id: 123 };
      jest.spyOn(ratingScaleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ratingScale });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ratingScaleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
