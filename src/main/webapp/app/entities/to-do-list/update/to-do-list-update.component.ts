import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ToDoListFormService, ToDoListFormGroup } from './to-do-list-form.service';
import { IToDoList } from '../to-do-list.model';
import { ToDoListService } from '../service/to-do-list.service';
import { IProfil } from 'app/entities/profil/profil.model';
import { ProfilService } from 'app/entities/profil/service/profil.service';
import { Status } from 'app/entities/enumerations/status.model';

@Component({
  selector: 'jhi-to-do-list-update',
  templateUrl: './to-do-list-update.component.html',
})
export class ToDoListUpdateComponent implements OnInit {
  isSaving = false;
  toDoList: IToDoList | null = null;
  statusValues = Object.keys(Status);

  profilsSharedCollection: IProfil[] = [];

  editForm: ToDoListFormGroup = this.toDoListFormService.createToDoListFormGroup();

  constructor(
    protected toDoListService: ToDoListService,
    protected toDoListFormService: ToDoListFormService,
    protected profilService: ProfilService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProfil = (o1: IProfil | null, o2: IProfil | null): boolean => this.profilService.compareProfil(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ toDoList }) => {
      this.toDoList = toDoList;
      if (toDoList) {
        this.updateForm(toDoList);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const toDoList = this.toDoListFormService.getToDoList(this.editForm);
    if (toDoList.id !== null) {
      this.subscribeToSaveResponse(this.toDoListService.update(toDoList));
    } else {
      this.subscribeToSaveResponse(this.toDoListService.create(toDoList));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IToDoList>>): void {
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

  protected updateForm(toDoList: IToDoList): void {
    this.toDoList = toDoList;
    this.toDoListFormService.resetForm(this.editForm, toDoList);

    this.profilsSharedCollection = this.profilService.addProfilToCollectionIfMissing<IProfil>(
      this.profilsSharedCollection,
      ...(toDoList.affectedTos ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.profilService
      .query()
      .pipe(map((res: HttpResponse<IProfil[]>) => res.body ?? []))
      .pipe(
        map((profils: IProfil[]) =>
          this.profilService.addProfilToCollectionIfMissing<IProfil>(profils, ...(this.toDoList?.affectedTos ?? []))
        )
      )
      .subscribe((profils: IProfil[]) => (this.profilsSharedCollection = profils));
  }
}
