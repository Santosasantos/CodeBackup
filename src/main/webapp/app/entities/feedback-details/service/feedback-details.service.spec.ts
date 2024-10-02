import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IFeedbackDetails } from '../feedback-details.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../feedback-details.test-samples';

import { FeedbackDetailsService } from './feedback-details.service';

const requireRestSample: IFeedbackDetails = {
  ...sampleWithRequiredData,
};

describe('FeedbackDetails Service', () => {
  let service: FeedbackDetailsService;
  let httpMock: HttpTestingController;
  let expectedResult: IFeedbackDetails | IFeedbackDetails[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FeedbackDetailsService);
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

    it('should create a FeedbackDetails', () => {
      const feedbackDetails = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(feedbackDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FeedbackDetails', () => {
      const feedbackDetails = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(feedbackDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FeedbackDetails', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FeedbackDetails', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FeedbackDetails', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFeedbackDetailsToCollectionIfMissing', () => {
      it('should add a FeedbackDetails to an empty array', () => {
        const feedbackDetails: IFeedbackDetails = sampleWithRequiredData;
        expectedResult = service.addFeedbackDetailsToCollectionIfMissing([], feedbackDetails);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(feedbackDetails);
      });

      it('should not add a FeedbackDetails to an array that contains it', () => {
        const feedbackDetails: IFeedbackDetails = sampleWithRequiredData;
        const feedbackDetailsCollection: IFeedbackDetails[] = [
          {
            ...feedbackDetails,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFeedbackDetailsToCollectionIfMissing(feedbackDetailsCollection, feedbackDetails);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FeedbackDetails to an array that doesn't contain it", () => {
        const feedbackDetails: IFeedbackDetails = sampleWithRequiredData;
        const feedbackDetailsCollection: IFeedbackDetails[] = [sampleWithPartialData];
        expectedResult = service.addFeedbackDetailsToCollectionIfMissing(feedbackDetailsCollection, feedbackDetails);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(feedbackDetails);
      });

      it('should add only unique FeedbackDetails to an array', () => {
        const feedbackDetailsArray: IFeedbackDetails[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const feedbackDetailsCollection: IFeedbackDetails[] = [sampleWithRequiredData];
        expectedResult = service.addFeedbackDetailsToCollectionIfMissing(feedbackDetailsCollection, ...feedbackDetailsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const feedbackDetails: IFeedbackDetails = sampleWithRequiredData;
        const feedbackDetails2: IFeedbackDetails = sampleWithPartialData;
        expectedResult = service.addFeedbackDetailsToCollectionIfMissing([], feedbackDetails, feedbackDetails2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(feedbackDetails);
        expect(expectedResult).toContain(feedbackDetails2);
      });

      it('should accept null and undefined values', () => {
        const feedbackDetails: IFeedbackDetails = sampleWithRequiredData;
        expectedResult = service.addFeedbackDetailsToCollectionIfMissing([], null, feedbackDetails, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(feedbackDetails);
      });

      it('should return initial array if no FeedbackDetails is added', () => {
        const feedbackDetailsCollection: IFeedbackDetails[] = [sampleWithRequiredData];
        expectedResult = service.addFeedbackDetailsToCollectionIfMissing(feedbackDetailsCollection, undefined, null);
        expect(expectedResult).toEqual(feedbackDetailsCollection);
      });
    });

    describe('compareFeedbackDetails', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFeedbackDetails(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFeedbackDetails(entity1, entity2);
        const compareResult2 = service.compareFeedbackDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFeedbackDetails(entity1, entity2);
        const compareResult2 = service.compareFeedbackDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFeedbackDetails(entity1, entity2);
        const compareResult2 = service.compareFeedbackDetails(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
