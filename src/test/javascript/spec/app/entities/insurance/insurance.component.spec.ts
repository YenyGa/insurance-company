import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { InsuranceTestModule } from '../../../test.module';
import { InsuranceComponent } from 'app/entities/insurance/insurance.component';
import { InsuranceService } from 'app/entities/insurance/insurance.service';
import { Insurance } from 'app/shared/model/insurance.model';

describe('Component Tests', () => {
  describe('Insurance Management Component', () => {
    let comp: InsuranceComponent;
    let fixture: ComponentFixture<InsuranceComponent>;
    let service: InsuranceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [InsuranceTestModule],
        declarations: [InsuranceComponent],
        providers: []
      })
        .overrideTemplate(InsuranceComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InsuranceComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(InsuranceService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Insurance(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.insurances[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
