import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IToDoList, NewToDoList } from '../to-do-list.model';

export type PartialUpdateToDoList = Partial<IToDoList> & Pick<IToDoList, 'id'>;

type RestOf<T extends IToDoList | NewToDoList> = Omit<T, 'creationTimestamp' | 'lastModificationTimestamp'> & {
  creationTimestamp?: string | null;
  lastModificationTimestamp?: string | null;
};

export type RestToDoList = RestOf<IToDoList>;

export type NewRestToDoList = RestOf<NewToDoList>;

export type PartialUpdateRestToDoList = RestOf<PartialUpdateToDoList>;

export type EntityResponseType = HttpResponse<IToDoList>;
export type EntityArrayResponseType = HttpResponse<IToDoList[]>;

@Injectable({ providedIn: 'root' })
export class ToDoListService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/to-do-lists');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(toDoList: NewToDoList): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(toDoList);
    return this.http
      .post<RestToDoList>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(toDoList: IToDoList): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(toDoList);
    return this.http
      .put<RestToDoList>(`${this.resourceUrl}/${this.getToDoListIdentifier(toDoList)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(toDoList: PartialUpdateToDoList): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(toDoList);
    return this.http
      .patch<RestToDoList>(`${this.resourceUrl}/${this.getToDoListIdentifier(toDoList)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestToDoList>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestToDoList[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getToDoListIdentifier(toDoList: Pick<IToDoList, 'id'>): string {
    return toDoList.id;
  }

  compareToDoList(o1: Pick<IToDoList, 'id'> | null, o2: Pick<IToDoList, 'id'> | null): boolean {
    return o1 && o2 ? this.getToDoListIdentifier(o1) === this.getToDoListIdentifier(o2) : o1 === o2;
  }

  addToDoListToCollectionIfMissing<Type extends Pick<IToDoList, 'id'>>(
    toDoListCollection: Type[],
    ...toDoListsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const toDoLists: Type[] = toDoListsToCheck.filter(isPresent);
    if (toDoLists.length > 0) {
      const toDoListCollectionIdentifiers = toDoListCollection.map(toDoListItem => this.getToDoListIdentifier(toDoListItem)!);
      const toDoListsToAdd = toDoLists.filter(toDoListItem => {
        const toDoListIdentifier = this.getToDoListIdentifier(toDoListItem);
        if (toDoListCollectionIdentifiers.includes(toDoListIdentifier)) {
          return false;
        }
        toDoListCollectionIdentifiers.push(toDoListIdentifier);
        return true;
      });
      return [...toDoListsToAdd, ...toDoListCollection];
    }
    return toDoListCollection;
  }

  protected convertDateFromClient<T extends IToDoList | NewToDoList | PartialUpdateToDoList>(toDoList: T): RestOf<T> {
    return {
      ...toDoList,
      creationTimestamp: toDoList.creationTimestamp?.toJSON() ?? null,
      lastModificationTimestamp: toDoList.lastModificationTimestamp?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restToDoList: RestToDoList): IToDoList {
    return {
      ...restToDoList,
      creationTimestamp: restToDoList.creationTimestamp ? dayjs(restToDoList.creationTimestamp) : undefined,
      lastModificationTimestamp: restToDoList.lastModificationTimestamp ? dayjs(restToDoList.lastModificationTimestamp) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestToDoList>): HttpResponse<IToDoList> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestToDoList[]>): HttpResponse<IToDoList[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
