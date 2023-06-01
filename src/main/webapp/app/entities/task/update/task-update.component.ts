import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { TaskFormService, TaskFormGroup } from './task-form.service';
import { ITask } from '../task.model';
import { TaskService } from '../service/task.service';
import { IToDoList } from 'app/entities/to-do-list/to-do-list.model';
import { ToDoListService } from 'app/entities/to-do-list/service/to-do-list.service';

@Component({
  selector: 'jhi-task-update',
  templateUrl: './task-update.component.html',
})
export class TaskUpdateComponent implements OnInit {
  isSaving = false;
  task: ITask | null = null;

  toDoListsSharedCollection: IToDoList[] = [];

  editForm: TaskFormGroup = this.taskFormService.createTaskFormGroup();

  constructor(
    protected taskService: TaskService,
    protected taskFormService: TaskFormService,
    protected toDoListService: ToDoListService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareToDoList = (o1: IToDoList | null, o2: IToDoList | null): boolean => this.toDoListService.compareToDoList(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ task }) => {
      this.task = task;
      if (task) {
        this.updateForm(task);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const task = this.taskFormService.getTask(this.editForm);
    if (task.id !== null) {
      this.subscribeToSaveResponse(this.taskService.update(task));
    } else {
      this.subscribeToSaveResponse(this.taskService.create(task));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITask>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(task: ITask): void {
    this.task = task;
    this.taskFormService.resetForm(this.editForm, task);

    this.toDoListsSharedCollection = this.toDoListService.addToDoListToCollectionIfMissing<IToDoList>(
      this.toDoListsSharedCollection,
      ...(task.toDos ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.toDoListService
      .query()
      .pipe(map((res: HttpResponse<IToDoList[]>) => res.body ?? []))
      .pipe(
        map((toDoLists: IToDoList[]) =>
          this.toDoListService.addToDoListToCollectionIfMissing<IToDoList>(toDoLists, ...(this.task?.toDos ?? []))
        )
      )
      .subscribe((toDoLists: IToDoList[]) => (this.toDoListsSharedCollection = toDoLists));
  }
}
