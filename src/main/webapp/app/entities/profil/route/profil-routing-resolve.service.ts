import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProfil } from '../profil.model';
import { ProfilService } from '../service/profil.service';

@Injectable({ providedIn: 'root' })
export class ProfilRoutingResolveService implements Resolve<IProfil | null> {
  constructor(protected service: ProfilService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProfil | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((profil: HttpResponse<IProfil>) => {
          if (profil.body) {
            return of(profil.body);
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
