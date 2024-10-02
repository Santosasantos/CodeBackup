import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IFeedbackSubType } from '../feedback-sub-type.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../feedback-sub-type.test-samples';

import { FeedbackSubTypeService } from './feedback-sub-type.service';

const requireRestSample: IFeedbackSubType = {
  ...sampleWithRequiredData,
};

describe('FeedbackSubType Service', () => {
  let service: FeedbackSubTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IFeedbackSubType | IFeedbackSubType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FeedbackSubTypeService);
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

    it('should create a FeedbackSubType', () => {
      const feedbackSubType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(feedbackSubType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FeedbackSubType', () => {
      const feedbackSubType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(feedbackSubType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FeedbackSubType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FeedbackSubType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FeedbackSubType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFeedbackSubTypeToCollectionIfMissing', () => {
      it('should add a FeedbackSubType to an empty array', () => {
        const feedbackSubType: IFeedbackSubType = sampleWithRequiredData;
        expectedResult = service.addFeedbackSubTypeToCollectionIfMissing([], feedbackSubType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(feedbackSubType);
      });

      it('should not add a FeedbackSubType to an array that contains it', () => {
        const feedbackSubType: IFeedbackSubType = sampleWithRequiredData;
        const feedbackSubTypeCollection: IFeedbackSubType[] = [
          {
            ...feedbackSubType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFeedbackSubTypeToCollectionIfMissing(feedbackSubTypeCollection, feedbackSubType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FeedbackSubType to an array that doesn't contain it", () => {
        const feedbackSubType: IFeedbackSubType = sampleWithRequiredData;
        const feedbackSubTypeCollection: IFeedbackSubType[] = [sampleWithPartialData];
        expectedResult = service.addFeedbackSubTypeToCollectionIfMissing(feedbackSubTypeCollection, feedbackSubType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(feedbackSubType);
      });

      it('should add only unique FeedbackSubType to an array', () => {
        const feedbackSubTypeArray: IFeedbackSubType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const feedbackSubTypeCollection: IFeedbackSubType[] = [sampleWithRequiredData];
        expectedResult = service.addFeedbackSubTypeToCollectionIfMissing(feedbackSubTypeCollection, ...feedbackSubTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const feedbackSubType: IFeedbackSubType = sampleWithRequiredData;
        const feedbackSubType2: IFeedbackSubType = sampleWithPartialData;
        expectedResult = service.addFeedbackSubTypeToCollectionIfMissing([], feedbackSubType, feedbackSubType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(feedbackSubType);
        expect(expectedResult).toContain(feedbackSubType2);
      });

      it('should accept null and undefined values', () => {
        const feedbackSubType: IFeedbackSubType = sampleWithRequiredData;
        expectedResult = service.addFeedbackSubTypeToCollectionIfMissing([], null, feedbackSubType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(feedbackSubType);
      });

      it('should return initial array if no FeedbackSubType is added', () => {
        const feedbackSubTypeCollection: IFeedbackSubType[] = [sampleWithRequiredData];
        expectedResult = service.addFeedbackSubTypeToCollectionIfMissing(feedbackSubTypeCollection, undefined, null);
        expect(expectedResult).toEqual(feedbackSubTypeCollection);
      });
    });

    describe('compareFeedbackSubType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFeedbackSubType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFeedbackSubType(entity1, entity2);
        const compareResult2 = service.compareFeedbackSubType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFeedbackSubType(entity1, entity2);
        const compareResult2 = service.compareFeedbackSubType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFeedbackSubType(entity1, entity2);
        const compareResult2 = service.compareFeedbackSubType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
