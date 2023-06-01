import { ProfilRole } from 'app/entities/enumerations/profil-role.model';

import { IProfil, NewProfil } from './profil.model';

export const sampleWithRequiredData: IProfil = {
  id: 'dc6a4f47-56e6-4a5b-8c86-a10a054753f5',
};

export const sampleWithPartialData: IProfil = {
  id: '0ccc34ba-877a-42cd-8b09-df353bf49732',
  name: 'Money maximize data-warehouse',
  role: ProfilRole['ACCOUNT_OWNER'],
};

export const sampleWithFullData: IProfil = {
  id: 'ab4ddc18-0bdd-4510-bc2f-ac5e57989b5f',
  name: 'Zloty',
  imageUrl: 'Generic Legacy El',
  role: ProfilRole['MEMBER'],
};

export const sampleWithNewData: NewProfil = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
