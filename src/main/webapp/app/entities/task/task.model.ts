import { IToDoList } from 'app/entities/to-do-list/to-do-list.model';

export interface ITask {
  id: string;
  isDone?: boolean | null;
  label?: string | null;
  toDos?: Pick<IToDoList, 'id'>[] | null;
}

export type NewTask = Omit<ITask, 'id'> & { id: null };
