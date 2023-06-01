import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ProfilFormService, ProfilFormGroup } from './profil-form.service';
import { IProfil } from '../profil.model';
import { ProfilService } from '../service/profil.service';
import { IToDoList } from 'app/entities/to-do-list/to-do-list.model';
import { ToDoListService } from 'app/entities/to-do-list/service/to-do-list.service';
import { ProfilRole } from 'app/entities/enumerations/profil-role.model';

@Component({
  selector: 'jhi-profil-update',
  templateUrl: './profil-update.component.html',
})
export class ProfilUpdateComponent implements OnInit {
  isSaving = false;
  profil: IProfil | null = null;
  profilRoleValues = Object.keys(ProfilRole);

  toDoListsSharedCollection: IToDoList[] = [];

  editForm: ProfilFormGroup = this.profilFormService.createProfilFormGroup();

  constructor(
    protected profilService: ProfilService,
    protected profilFormService: ProfilFormService,
    protected toDoListService: ToDoListService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareToDoList = (o1: IToDoList | null, o2: IToDoList | null): boolean => this.toDoListService.compareToDoList(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ profil }) => {
      this.profil = profil;
      if (profil) {
        this.updateForm(profil);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const profil = this.profilFormService.getProfil(this.editForm);
    if (profil.id !== null) {
      this.subscribeToSaveResponse(this.profilService.update(profil));
    } else {
      this.subscribeToSaveResponse(this.profilService.create(profil));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProfil>>): void {
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

  protected updateForm(profil: IProfil): void {
    this.profil = profil;
    this.profilFormService.resetForm(this.editForm, profil);

    this.toDoListsSharedCollection = this.toDoListService.addToDoListToCollectionIfMissing<IToDoList>(
      this.toDoListsSharedCollection,
      profil.toDoList
    );
  }

  protected loadRelationshipsOptions(): void {
    this.toDoListService
      .query()
      .pipe(map((res: HttpResponse<IToDoList[]>) => res.body ?? []))
      .pipe(
        map((toDoLists: IToDoList[]) => this.toDoListService.addToDoListToCollectionIfMissing<IToDoList>(toDoLists, this.profil?.toDoList))
      )
      .subscribe((toDoLists: IToDoList[]) => (this.toDoListsSharedCollection = toDoLists));
  }
}
