import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { InsuranceTestModule } from '../../../test.module';
import { InsuranceUpdateComponent } from 'app/entities/insurance/insurance-update.component';
import { InsuranceService } from 'app/entities/insurance/insurance.service';
import { Insurance } from 'app/shared/model/insurance.model';

describe('Component Tests', () => {
  describe('Insurance Management Update Component', () => {
    let comp: InsuranceUpdateComponent;
    let fixture: ComponentFixture<InsuranceUpdateComponent>;
    let service: InsuranceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [InsuranceTestModule],
        declarations: [InsuranceUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(InsuranceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InsuranceUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(InsuranceService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Insurance(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Insurance();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
