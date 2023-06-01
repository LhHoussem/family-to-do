import { ITask, NewTask } from './task.model';

export const sampleWithRequiredData: ITask = {
  id: '6cd369ff-3e50-44dc-bff2-85e0c26ef9ed',
};

export const sampleWithPartialData: ITask = {
  id: 'c51c0fba-8dba-4c51-b5b3-279bc9af4bf8',
  isDone: false,
  label: 'up',
};

export const sampleWithFullData: ITask = {
  id: '49af2177-3441-4181-8297-ee023b64e2d6',
  isDone: true,
  label: 'Movies',
};

export const sampleWithNewData: NewTask = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
