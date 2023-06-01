import dayjs from 'dayjs/esm';

import { Status } from 'app/entities/enumerations/status.model';

import { IToDoList, NewToDoList } from './to-do-list.model';

export const sampleWithRequiredData: IToDoList = {
  id: 'b1a288f1-06b9-4d72-9e31-979c78da4c42',
};

export const sampleWithPartialData: IToDoList = {
  id: '840c7534-b0a4-4bcf-8830-3e1b0d69b9bd',
  label: 'West Wooden',
  lastModificationTimestamp: dayjs('2023-05-31T21:10'),
};

export const sampleWithFullData: IToDoList = {
  id: '2740d76d-c10c-4375-a785-98573d46f799',
  label: 'Industrial Money',
  status: Status['OPENED'],
  creationTimestamp: dayjs('2023-06-01T03:33'),
  lastModificationTimestamp: dayjs('2023-05-31T20:41'),
};

export const sampleWithNewData: NewToDoList = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
