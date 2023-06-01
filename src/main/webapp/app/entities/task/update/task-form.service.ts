import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITask, NewTask } from '../task.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITask for edit and NewTaskFormGroupInput for create.
 */
type TaskFormGroupInput = ITask | PartialWithRequiredKeyOf<NewTask>;

type TaskFormDefaults = Pick<NewTask, 'id' | 'isDone'>;

type TaskFormGroupContent = {
  id: FormControl<ITask['id'] | NewTask['id']>;
  isDone: FormControl<ITask['isDone']>;
  label: FormControl<ITask['label']>;
  toDoList: FormControl<ITask['toDoList']>;
};

export type TaskFormGroup = FormGroup<TaskFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TaskFormService {
  createTaskFormGroup(task: TaskFormGroupInput = { id: null }): TaskFormGroup {
    const taskRawValue = {
      ...this.getFormDefaults(),
      ...task,
    };
    return new FormGroup<TaskFormGroupContent>({
      id: new FormControl(
        { value: taskRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      isDone: new FormControl(taskRawValue.isDone),
      label: new FormControl(taskRawValue.label),
      toDoList: new FormControl(taskRawValue.toDoList),
    });
  }

  getTask(form: TaskFormGroup): ITask | NewTask {
    return form.getRawValue() as ITask | NewTask;
  }

  resetForm(form: TaskFormGroup, task: TaskFormGroupInput): void {
    const taskRawValue = { ...this.getFormDefaults(), ...task };
    form.reset(
      {
        ...taskRawValue,
        id: { value: taskRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TaskFormDefaults {
    return {
      id: null,
      isDone: false,
    };
  }
}
