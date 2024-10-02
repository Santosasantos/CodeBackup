import { TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IFeedbackSubType } from '../feedback-sub-type.model';
import { FeedbackSubTypeService } from '../service/feedback-sub-type.service';

import feedbackSubTypeResolve from './feedback-sub-type-routing-resolve.service';

describe('FeedbackSubType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: FeedbackSubTypeService;
  let resultFeedbackSubType: IFeedbackSubType | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(FeedbackSubTypeService);
    resultFeedbackSubType = undefined;
  });

  describe('resolve', () => {
    it('should return IFeedbackSubType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        feedbackSubTypeResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultFeedbackSubType = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultFeedbackSubType).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        feedbackSubTypeResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultFeedbackSubType = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFeedbackSubType).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IFeedbackSubType>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        feedbackSubTypeResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultFeedbackSubType = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultFeedbackSubType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
