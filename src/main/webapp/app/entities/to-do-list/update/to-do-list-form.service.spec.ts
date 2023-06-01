import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../to-do-list.test-samples';

import { ToDoListFormService } from './to-do-list-form.service';

describe('ToDoList Form Service', () => {
  let service: ToDoListFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ToDoListFormService);
  });

  describe('Service methods', () => {
    describe('createToDoListFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createToDoListFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            label: expect.any(Object),
            status: expect.any(Object),
            creationTimestamp: expect.any(Object),
            lastModificationTimestamp: expect.any(Object),
            affectedTos: expect.any(Object),
            owner: expect.any(Object),
          })
        );
      });

      it('passing IToDoList should create a new form with FormGroup', () => {
        const formGroup = service.createToDoListFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            label: expect.any(Object),
            status: expect.any(Object),
            creationTimestamp: expect.any(Object),
            lastModificationTimestamp: expect.any(Object),
            affectedTos: expect.any(Object),
            owner: expect.any(Object),
          })
        );
      });
    });

    describe('getToDoList', () => {
      it('should return NewToDoList for default ToDoList initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createToDoListFormGroup(sampleWithNewData);

        const toDoList = service.getToDoList(formGroup) as any;

        expect(toDoList).toMatchObject(sampleWithNewData);
      });

      it('should return NewToDoList for empty ToDoList initial value', () => {
        const formGroup = service.createToDoListFormGroup();

        const toDoList = service.getToDoList(formGroup) as any;

        expect(toDoList).toMatchObject({});
      });

      it('should return IToDoList', () => {
        const formGroup = service.createToDoListFormGroup(sampleWithRequiredData);

        const toDoList = service.getToDoList(formGroup) as any;

        expect(toDoList).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IToDoList should not enable id FormControl', () => {
        const formGroup = service.createToDoListFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewToDoList should disable id FormControl', () => {
        const formGroup = service.createToDoListFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
