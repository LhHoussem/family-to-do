import { IToDoList } from 'app/entities/to-do-list/to-do-list.model';
import { ProfilRole } from 'app/entities/enumerations/profil-role.model';

export interface IProfil {
  id: string;
  name?: string | null;
  imageUrl?: string | null;
  role?: ProfilRole | null;
  toDoList?: Pick<IToDoList, 'id'> | null;
  affecteds?: Pick<IToDoList, 'id'>[] | null;
}

export type NewProfil = Omit<IProfil, 'id'> & { id: null };
