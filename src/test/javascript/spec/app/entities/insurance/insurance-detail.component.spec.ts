import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InsuranceTestModule } from '../../../test.module';
import { InsuranceDetailComponent } from 'app/entities/insurance/insurance-detail.component';
import { Insurance } from 'app/shared/model/insurance.model';

describe('Component Tests', () => {
  describe('Insurance Management Detail Component', () => {
    let comp: InsuranceDetailComponent;
    let fixture: ComponentFixture<InsuranceDetailComponent>;
    const route = ({ data: of({ insurance: new Insurance(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [InsuranceTestModule],
        declarations: [InsuranceDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(InsuranceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InsuranceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.insurance).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
