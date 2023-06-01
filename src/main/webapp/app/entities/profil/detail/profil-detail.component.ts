import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProfil } from '../profil.model';

@Component({
  selector: 'jhi-profil-detail',
  templateUrl: './profil-detail.component.html',
})
export class ProfilDetailComponent implements OnInit {
  profil: IProfil | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ profil }) => {
      this.profil = profil;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
