import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProfil, NewProfil } from '../profil.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProfil for edit and NewProfilFormGroupInput for create.
 */
type ProfilFormGroupInput = IProfil | PartialWithRequiredKeyOf<NewProfil>;

type ProfilFormDefaults = Pick<NewProfil, 'id' | 'affecteds'>;

type ProfilFormGroupContent = {
  id: FormControl<IProfil['id'] | NewProfil['id']>;
  name: FormControl<IProfil['name']>;
  imageUrl: FormControl<IProfil['imageUrl']>;
  role: FormControl<IProfil['role']>;
  toDoList: FormControl<IProfil['toDoList']>;
  affecteds: FormControl<IProfil['affecteds']>;
};

export type ProfilFormGroup = FormGroup<ProfilFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProfilFormService {
  createProfilFormGroup(profil: ProfilFormGroupInput = { id: null }): ProfilFormGroup {
    const profilRawValue = {
      ...this.getFormDefaults(),
      ...profil,
    };
    return new FormGroup<ProfilFormGroupContent>({
      id: new FormControl(
        { value: profilRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(profilRawValue.name),
      imageUrl: new FormControl(profilRawValue.imageUrl),
      role: new FormControl(profilRawValue.role),
      toDoList: new FormControl(profilRawValue.toDoList),
      affecteds: new FormControl(profilRawValue.affecteds ?? []),
    });
  }

  getProfil(form: ProfilFormGroup): IProfil | NewProfil {
    return form.getRawValue() as IProfil | NewProfil;
  }

  resetForm(form: ProfilFormGroup, profil: ProfilFormGroupInput): void {
    const profilRawValue = { ...this.getFormDefaults(), ...profil };
    form.reset(
      {
        ...profilRawValue,
        id: { value: profilRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProfilFormDefaults {
    return {
      id: null,
      affecteds: [],
    };
  }
}
