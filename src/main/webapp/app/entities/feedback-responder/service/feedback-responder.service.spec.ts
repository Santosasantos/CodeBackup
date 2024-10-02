import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IFeedbackResponder } from '../feedback-responder.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../feedback-responder.test-samples';

import { FeedbackResponderService } from './feedback-responder.service';

const requireRestSample: IFeedbackResponder = {
  ...sampleWithRequiredData,
};

describe('FeedbackResponder Service', () => {
  let service: FeedbackResponderService;
  let httpMock: HttpTestingController;
  let expectedResult: IFeedbackResponder | IFeedbackResponder[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FeedbackResponderService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a FeedbackResponder', () => {
      const feedbackResponder = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(feedbackResponder).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FeedbackResponder', () => {
      const feedbackResponder = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(feedbackResponder).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FeedbackResponder', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FeedbackResponder', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FeedbackResponder', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFeedbackResponderToCollectionIfMissing', () => {
      it('should add a FeedbackResponder to an empty array', () => {
        const feedbackResponder: IFeedbackResponder = sampleWithRequiredData;
        expectedResult = service.addFeedbackResponderToCollectionIfMissing([], feedbackResponder);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(feedbackResponder);
      });

      it('should not add a FeedbackResponder to an array that contains it', () => {
        const feedbackResponder: IFeedbackResponder = sampleWithRequiredData;
        const feedbackResponderCollection: IFeedbackResponder[] = [
          {
            ...feedbackResponder,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFeedbackResponderToCollectionIfMissing(feedbackResponderCollection, feedbackResponder);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FeedbackResponder to an array that doesn't contain it", () => {
        const feedbackResponder: IFeedbackResponder = sampleWithRequiredData;
        const feedbackResponderCollection: IFeedbackResponder[] = [sampleWithPartialData];
        expectedResult = service.addFeedbackResponderToCollectionIfMissing(feedbackResponderCollection, feedbackResponder);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(feedbackResponder);
      });

      it('should add only unique FeedbackResponder to an array', () => {
        const feedbackResponderArray: IFeedbackResponder[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const feedbackResponderCollection: IFeedbackResponder[] = [sampleWithRequiredData];
        expectedResult = service.addFeedbackResponderToCollectionIfMissing(feedbackResponderCollection, ...feedbackResponderArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const feedbackResponder: IFeedbackResponder = sampleWithRequiredData;
        const feedbackResponder2: IFeedbackResponder = sampleWithPartialData;
        expectedResult = service.addFeedbackResponderToCollectionIfMissing([], feedbackResponder, feedbackResponder2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(feedbackResponder);
        expect(expectedResult).toContain(feedbackResponder2);
      });

      it('should accept null and undefined values', () => {
        const feedbackResponder: IFeedbackResponder = sampleWithRequiredData;
        expectedResult = service.addFeedbackResponderToCollectionIfMissing([], null, feedbackResponder, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(feedbackResponder);
      });

      it('should return initial array if no FeedbackResponder is added', () => {
        const feedbackResponderCollection: IFeedbackResponder[] = [sampleWithRequiredData];
        expectedResult = service.addFeedbackResponderToCollectionIfMissing(feedbackResponderCollection, undefined, null);
        expect(expectedResult).toEqual(feedbackResponderCollection);
      });
    });

    describe('compareFeedbackResponder', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFeedbackResponder(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFeedbackResponder(entity1, entity2);
        const compareResult2 = service.compareFeedbackResponder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFeedbackResponder(entity1, entity2);
        const compareResult2 = service.compareFeedbackResponder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFeedbackResponder(entity1, entity2);
        const compareResult2 = service.compareFeedbackResponder(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
