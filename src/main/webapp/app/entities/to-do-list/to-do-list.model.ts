import dayjs from 'dayjs/esm';
import { IProfil } from 'app/entities/profil/profil.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IToDoList {
  id: string;
  label?: string | null;
  status?: Status | null;
  creationTimestamp?: dayjs.Dayjs | null;
  lastModificationTimestamp?: dayjs.Dayjs | null;
  affectedTos?: Pick<IProfil, 'id'>[] | null;
}

export type NewToDoList = Omit<IToDoList, 'id'> & { id: null };
