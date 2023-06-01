import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IToDoList } from '../to-do-list.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../to-do-list.test-samples';

import { ToDoListService, RestToDoList } from './to-do-list.service';

const requireRestSample: RestToDoList = {
  ...sampleWithRequiredData,
  creationTimestamp: sampleWithRequiredData.creationTimestamp?.toJSON(),
  lastModificationTimestamp: sampleWithRequiredData.lastModificationTimestamp?.toJSON(),
};

describe('ToDoList Service', () => {
  let service: ToDoListService;
  let httpMock: HttpTestingController;
  let expectedResult: IToDoList | IToDoList[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ToDoListService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ToDoList', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const toDoList = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(toDoList).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ToDoList', () => {
      const toDoList = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(toDoList).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ToDoList', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ToDoList', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ToDoList', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addToDoListToCollectionIfMissing', () => {
      it('should add a ToDoList to an empty array', () => {
        const toDoList: IToDoList = sampleWithRequiredData;
        expectedResult = service.addToDoListToCollectionIfMissing([], toDoList);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(toDoList);
      });

      it('should not add a ToDoList to an array that contains it', () => {
        const toDoList: IToDoList = sampleWithRequiredData;
        const toDoListCollection: IToDoList[] = [
          {
            ...toDoList,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addToDoListToCollectionIfMissing(toDoListCollection, toDoList);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ToDoList to an array that doesn't contain it", () => {
        const toDoList: IToDoList = sampleWithRequiredData;
        const toDoListCollection: IToDoList[] = [sampleWithPartialData];
        expectedResult = service.addToDoListToCollectionIfMissing(toDoListCollection, toDoList);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(toDoList);
      });

      it('should add only unique ToDoList to an array', () => {
        const toDoListArray: IToDoList[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const toDoListCollection: IToDoList[] = [sampleWithRequiredData];
        expectedResult = service.addToDoListToCollectionIfMissing(toDoListCollection, ...toDoListArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const toDoList: IToDoList = sampleWithRequiredData;
        const toDoList2: IToDoList = sampleWithPartialData;
        expectedResult = service.addToDoListToCollectionIfMissing([], toDoList, toDoList2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(toDoList);
        expect(expectedResult).toContain(toDoList2);
      });

      it('should accept null and undefined values', () => {
        const toDoList: IToDoList = sampleWithRequiredData;
        expectedResult = service.addToDoListToCollectionIfMissing([], null, toDoList, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(toDoList);
      });

      it('should return initial array if no ToDoList is added', () => {
        const toDoListCollection: IToDoList[] = [sampleWithRequiredData];
        expectedResult = service.addToDoListToCollectionIfMissing(toDoListCollection, undefined, null);
        expect(expectedResult).toEqual(toDoListCollection);
      });
    });

    describe('compareToDoList', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareToDoList(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareToDoList(entity1, entity2);
        const compareResult2 = service.compareToDoList(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareToDoList(entity1, entity2);
        const compareResult2 = service.compareToDoList(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareToDoList(entity1, entity2);
        const compareResult2 = service.compareToDoList(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
