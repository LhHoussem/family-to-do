import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProfilDetailComponent } from './profil-detail.component';

describe('Profil Management Detail Component', () => {
  let comp: ProfilDetailComponent;
  let fixture: ComponentFixture<ProfilDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProfilDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ profil: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(ProfilDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ProfilDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load profil on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.profil).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
