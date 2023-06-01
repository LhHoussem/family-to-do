import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IToDoList } from '../to-do-list.model';
import { ToDoListService } from '../service/to-do-list.service';

@Injectable({ providedIn: 'root' })
export class ToDoListRoutingResolveService implements Resolve<IToDoList | null> {
  constructor(protected service: ToDoListService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IToDoList | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((toDoList: HttpResponse<IToDoList>) => {
          if (toDoList.body) {
            return of(toDoList.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
