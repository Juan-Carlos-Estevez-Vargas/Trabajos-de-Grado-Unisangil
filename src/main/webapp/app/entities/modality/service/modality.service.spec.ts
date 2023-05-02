import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IModality } from '../modality.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../modality.test-samples';

import { ModalityService } from './modality.service';

const requireRestSample: IModality = {
  ...sampleWithRequiredData,
};

describe('Modality Service', () => {
  let service: ModalityService;
  let httpMock: HttpTestingController;
  let expectedResult: IModality | IModality[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ModalityService);
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

    it('should create a Modality', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const modality = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(modality).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Modality', () => {
      const modality = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(modality).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Modality', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Modality', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Modality', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addModalityToCollectionIfMissing', () => {
      it('should add a Modality to an empty array', () => {
        const modality: IModality = sampleWithRequiredData;
        expectedResult = service.addModalityToCollectionIfMissing([], modality);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(modality);
      });

      it('should not add a Modality to an array that contains it', () => {
        const modality: IModality = sampleWithRequiredData;
        const modalityCollection: IModality[] = [
          {
            ...modality,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addModalityToCollectionIfMissing(modalityCollection, modality);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Modality to an array that doesn't contain it", () => {
        const modality: IModality = sampleWithRequiredData;
        const modalityCollection: IModality[] = [sampleWithPartialData];
        expectedResult = service.addModalityToCollectionIfMissing(modalityCollection, modality);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(modality);
      });

      it('should add only unique Modality to an array', () => {
        const modalityArray: IModality[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const modalityCollection: IModality[] = [sampleWithRequiredData];
        expectedResult = service.addModalityToCollectionIfMissing(modalityCollection, ...modalityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const modality: IModality = sampleWithRequiredData;
        const modality2: IModality = sampleWithPartialData;
        expectedResult = service.addModalityToCollectionIfMissing([], modality, modality2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(modality);
        expect(expectedResult).toContain(modality2);
      });

      it('should accept null and undefined values', () => {
        const modality: IModality = sampleWithRequiredData;
        expectedResult = service.addModalityToCollectionIfMissing([], null, modality, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(modality);
      });

      it('should return initial array if no Modality is added', () => {
        const modalityCollection: IModality[] = [sampleWithRequiredData];
        expectedResult = service.addModalityToCollectionIfMissing(modalityCollection, undefined, null);
        expect(expectedResult).toEqual(modalityCollection);
      });
    });

    describe('compareModality', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareModality(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareModality(entity1, entity2);
        const compareResult2 = service.compareModality(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareModality(entity1, entity2);
        const compareResult2 = service.compareModality(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareModality(entity1, entity2);
        const compareResult2 = service.compareModality(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
