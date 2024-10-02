import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IRatingScale } from '../rating-scale.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../rating-scale.test-samples';

import { RatingScaleService } from './rating-scale.service';

const requireRestSample: IRatingScale = {
  ...sampleWithRequiredData,
};

describe('RatingScale Service', () => {
  let service: RatingScaleService;
  let httpMock: HttpTestingController;
  let expectedResult: IRatingScale | IRatingScale[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(RatingScaleService);
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

    it('should create a RatingScale', () => {
      const ratingScale = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ratingScale).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RatingScale', () => {
      const ratingScale = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ratingScale).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RatingScale', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RatingScale', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RatingScale', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRatingScaleToCollectionIfMissing', () => {
      it('should add a RatingScale to an empty array', () => {
        const ratingScale: IRatingScale = sampleWithRequiredData;
        expectedResult = service.addRatingScaleToCollectionIfMissing([], ratingScale);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ratingScale);
      });

      it('should not add a RatingScale to an array that contains it', () => {
        const ratingScale: IRatingScale = sampleWithRequiredData;
        const ratingScaleCollection: IRatingScale[] = [
          {
            ...ratingScale,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRatingScaleToCollectionIfMissing(ratingScaleCollection, ratingScale);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RatingScale to an array that doesn't contain it", () => {
        const ratingScale: IRatingScale = sampleWithRequiredData;
        const ratingScaleCollection: IRatingScale[] = [sampleWithPartialData];
        expectedResult = service.addRatingScaleToCollectionIfMissing(ratingScaleCollection, ratingScale);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ratingScale);
      });

      it('should add only unique RatingScale to an array', () => {
        const ratingScaleArray: IRatingScale[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ratingScaleCollection: IRatingScale[] = [sampleWithRequiredData];
        expectedResult = service.addRatingScaleToCollectionIfMissing(ratingScaleCollection, ...ratingScaleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ratingScale: IRatingScale = sampleWithRequiredData;
        const ratingScale2: IRatingScale = sampleWithPartialData;
        expectedResult = service.addRatingScaleToCollectionIfMissing([], ratingScale, ratingScale2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ratingScale);
        expect(expectedResult).toContain(ratingScale2);
      });

      it('should accept null and undefined values', () => {
        const ratingScale: IRatingScale = sampleWithRequiredData;
        expectedResult = service.addRatingScaleToCollectionIfMissing([], null, ratingScale, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ratingScale);
      });

      it('should return initial array if no RatingScale is added', () => {
        const ratingScaleCollection: IRatingScale[] = [sampleWithRequiredData];
        expectedResult = service.addRatingScaleToCollectionIfMissing(ratingScaleCollection, undefined, null);
        expect(expectedResult).toEqual(ratingScaleCollection);
      });
    });

    describe('compareRatingScale', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRatingScale(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRatingScale(entity1, entity2);
        const compareResult2 = service.compareRatingScale(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRatingScale(entity1, entity2);
        const compareResult2 = service.compareRatingScale(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRatingScale(entity1, entity2);
        const compareResult2 = service.compareRatingScale(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
