import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IFeedbackType } from '../feedback-type.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../feedback-type.test-samples';

import { FeedbackTypeService } from './feedback-type.service';

const requireRestSample: IFeedbackType = {
  ...sampleWithRequiredData,
};

describe('FeedbackType Service', () => {
  let service: FeedbackTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IFeedbackType | IFeedbackType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FeedbackTypeService);
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

    it('should create a FeedbackType', () => {
      const feedbackType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(feedbackType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FeedbackType', () => {
      const feedbackType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(feedbackType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FeedbackType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FeedbackType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FeedbackType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFeedbackTypeToCollectionIfMissing', () => {
      it('should add a FeedbackType to an empty array', () => {
        const feedbackType: IFeedbackType = sampleWithRequiredData;
        expectedResult = service.addFeedbackTypeToCollectionIfMissing([], feedbackType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(feedbackType);
      });

      it('should not add a FeedbackType to an array that contains it', () => {
        const feedbackType: IFeedbackType = sampleWithRequiredData;
        const feedbackTypeCollection: IFeedbackType[] = [
          {
            ...feedbackType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFeedbackTypeToCollectionIfMissing(feedbackTypeCollection, feedbackType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FeedbackType to an array that doesn't contain it", () => {
        const feedbackType: IFeedbackType = sampleWithRequiredData;
        const feedbackTypeCollection: IFeedbackType[] = [sampleWithPartialData];
        expectedResult = service.addFeedbackTypeToCollectionIfMissing(feedbackTypeCollection, feedbackType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(feedbackType);
      });

      it('should add only unique FeedbackType to an array', () => {
        const feedbackTypeArray: IFeedbackType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const feedbackTypeCollection: IFeedbackType[] = [sampleWithRequiredData];
        expectedResult = service.addFeedbackTypeToCollectionIfMissing(feedbackTypeCollection, ...feedbackTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const feedbackType: IFeedbackType = sampleWithRequiredData;
        const feedbackType2: IFeedbackType = sampleWithPartialData;
        expectedResult = service.addFeedbackTypeToCollectionIfMissing([], feedbackType, feedbackType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(feedbackType);
        expect(expectedResult).toContain(feedbackType2);
      });

      it('should accept null and undefined values', () => {
        const feedbackType: IFeedbackType = sampleWithRequiredData;
        expectedResult = service.addFeedbackTypeToCollectionIfMissing([], null, feedbackType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(feedbackType);
      });

      it('should return initial array if no FeedbackType is added', () => {
        const feedbackTypeCollection: IFeedbackType[] = [sampleWithRequiredData];
        expectedResult = service.addFeedbackTypeToCollectionIfMissing(feedbackTypeCollection, undefined, null);
        expect(expectedResult).toEqual(feedbackTypeCollection);
      });
    });

    describe('compareFeedbackType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFeedbackType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFeedbackType(entity1, entity2);
        const compareResult2 = service.compareFeedbackType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFeedbackType(entity1, entity2);
        const compareResult2 = service.compareFeedbackType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFeedbackType(entity1, entity2);
        const compareResult2 = service.compareFeedbackType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
