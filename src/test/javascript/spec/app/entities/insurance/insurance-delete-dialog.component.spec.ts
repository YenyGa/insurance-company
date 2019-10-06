import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { InsuranceTestModule } from '../../../test.module';
import { InsuranceDeleteDialogComponent } from 'app/entities/insurance/insurance-delete-dialog.component';
import { InsuranceService } from 'app/entities/insurance/insurance.service';

describe('Component Tests', () => {
  describe('Insurance Management Delete Component', () => {
    let comp: InsuranceDeleteDialogComponent;
    let fixture: ComponentFixture<InsuranceDeleteDialogComponent>;
    let service: InsuranceService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [InsuranceTestModule],
        declarations: [InsuranceDeleteDialogComponent]
      })
        .overrideTemplate(InsuranceDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InsuranceDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(InsuranceService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
