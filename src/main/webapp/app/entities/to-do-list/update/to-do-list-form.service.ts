import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IToDoList, NewToDoList } from '../to-do-list.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IToDoList for edit and NewToDoListFormGroupInput for create.
 */
type ToDoListFormGroupInput = IToDoList | PartialWithRequiredKeyOf<NewToDoList>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IToDoList | NewToDoList> = Omit<T, 'creationTimestamp' | 'lastModificationTimestamp'> & {
  creationTimestamp?: string | null;
  lastModificationTimestamp?: string | null;
};

type ToDoListFormRawValue = FormValueOf<IToDoList>;

type NewToDoListFormRawValue = FormValueOf<NewToDoList>;

type ToDoListFormDefaults = Pick<NewToDoList, 'id' | 'creationTimestamp' | 'lastModificationTimestamp' | 'affectedTos'>;

type ToDoListFormGroupContent = {
  id: FormControl<ToDoListFormRawValue['id'] | NewToDoList['id']>;
  label: FormControl<ToDoListFormRawValue['label']>;
  status: FormControl<ToDoListFormRawValue['status']>;
  creationTimestamp: FormControl<ToDoListFormRawValue['creationTimestamp']>;
  lastModificationTimestamp: FormControl<ToDoListFormRawValue['lastModificationTimestamp']>;
  affectedTos: FormControl<ToDoListFormRawValue['affectedTos']>;
  owner: FormControl<ToDoListFormRawValue['owner']>;
};

export type ToDoListFormGroup = FormGroup<ToDoListFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ToDoListFormService {
  createToDoListFormGroup(toDoList: ToDoListFormGroupInput = { id: null }): ToDoListFormGroup {
    const toDoListRawValue = this.convertToDoListToToDoListRawValue({
      ...this.getFormDefaults(),
      ...toDoList,
    });
    return new FormGroup<ToDoListFormGroupContent>({
      id: new FormControl(
        { value: toDoListRawValue.id, disabled: toDoListRawValue.id !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      label: new FormControl(toDoListRawValue.label),
      status: new FormControl(toDoListRawValue.status),
      creationTimestamp: new FormControl(toDoListRawValue.creationTimestamp),
      lastModificationTimestamp: new FormControl(toDoListRawValue.lastModificationTimestamp),
      affectedTos: new FormControl(toDoListRawValue.affectedTos ?? []),
      owner: new FormControl(toDoListRawValue.owner),
    });
  }

  getToDoList(form: ToDoListFormGroup): IToDoList | NewToDoList {
    return this.convertToDoListRawValueToToDoList(form.getRawValue() as ToDoListFormRawValue | NewToDoListFormRawValue);
  }

  resetForm(form: ToDoListFormGroup, toDoList: ToDoListFormGroupInput): void {
    const toDoListRawValue = this.convertToDoListToToDoListRawValue({ ...this.getFormDefaults(), ...toDoList });
    form.reset(
      {
        ...toDoListRawValue,
        id: { value: toDoListRawValue.id, disabled: toDoListRawValue.id !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ToDoListFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      creationTimestamp: currentTime,
      lastModificationTimestamp: currentTime,
      affectedTos: [],
    };
  }

  private convertToDoListRawValueToToDoList(rawToDoList: ToDoListFormRawValue | NewToDoListFormRawValue): IToDoList | NewToDoList {
    return {
      ...rawToDoList,
      creationTimestamp: dayjs(rawToDoList.creationTimestamp, DATE_TIME_FORMAT),
      lastModificationTimestamp: dayjs(rawToDoList.lastModificationTimestamp, DATE_TIME_FORMAT),
    };
  }

  private convertToDoListToToDoListRawValue(
    toDoList: IToDoList | (Partial<NewToDoList> & ToDoListFormDefaults)
  ): ToDoListFormRawValue | PartialWithRequiredKeyOf<NewToDoListFormRawValue> {
    return {
      ...toDoList,
      creationTimestamp: toDoList.creationTimestamp ? toDoList.creationTimestamp.format(DATE_TIME_FORMAT) : undefined,
      lastModificationTimestamp: toDoList.lastModificationTimestamp
        ? toDoList.lastModificationTimestamp.format(DATE_TIME_FORMAT)
        : undefined,
      affectedTos: toDoList.affectedTos ?? [],
    };
  }
}
