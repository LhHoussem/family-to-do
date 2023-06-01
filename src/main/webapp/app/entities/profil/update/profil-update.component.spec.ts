import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProfilFormService } from './profil-form.service';
import { ProfilService } from '../service/profil.service';
import { IProfil } from '../profil.model';

import { ProfilUpdateComponent } from './profil-update.component';

describe('Profil Management Update Component', () => {
  let comp: ProfilUpdateComponent;
  let fixture: ComponentFixture<ProfilUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let profilFormService: ProfilFormService;
  let profilService: ProfilService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProfilUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ProfilUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProfilUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    profilFormService = TestBed.inject(ProfilFormService);
    profilService = TestBed.inject(ProfilService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const profil: IProfil = { id: 'CBA' };

      activatedRoute.data = of({ profil });
      comp.ngOnInit();

      expect(comp.profil).toEqual(profil);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProfil>>();
      const profil = { id: 'ABC' };
      jest.spyOn(profilFormService, 'getProfil').mockReturnValue(profil);
      jest.spyOn(profilService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ profil });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: profil }));
      saveSubject.complete();

      // THEN
      expect(profilFormService.getProfil).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(profilService.update).toHaveBeenCalledWith(expect.objectContaining(profil));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProfil>>();
      const profil = { id: 'ABC' };
      jest.spyOn(profilFormService, 'getProfil').mockReturnValue({ id: null });
      jest.spyOn(profilService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ profil: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: profil }));
      saveSubject.complete();

      // THEN
      expect(profilFormService.getProfil).toHaveBeenCalled();
      expect(profilService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProfil>>();
      const profil = { id: 'ABC' };
      jest.spyOn(profilService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ profil });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(profilService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
