import { TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IFeedbackResponder } from '../feedback-responder.model';
import { FeedbackResponderService } from '../service/feedback-responder.service';

import feedbackResponderResolve from './feedback-responder-routing-resolve.service';

describe('FeedbackResponder routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: FeedbackResponderService;
  let resultFeedbackResponder: IFeedbackResponder | null | undefined;

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
    service = TestBed.inject(FeedbackResponderService);
    resultFeedbackResponder = undefined;
  });

  describe('resolve', () => {
    it('should return IFeedbackResponder returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        feedbackResponderResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultFeedbackResponder = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultFeedbackResponder).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        feedbackResponderResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultFeedbackResponder = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFeedbackResponder).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IFeedbackResponder>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        feedbackResponderResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultFeedbackResponder = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultFeedbackResponder).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
