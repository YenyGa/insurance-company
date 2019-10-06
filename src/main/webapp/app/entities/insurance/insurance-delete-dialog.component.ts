import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IInsurance } from 'app/shared/model/insurance.model';
import { InsuranceService } from './insurance.service';

@Component({
  selector: 'jhi-insurance-delete-dialog',
  templateUrl: './insurance-delete-dialog.component.html'
})
export class InsuranceDeleteDialogComponent {
  insurance: IInsurance;

  constructor(protected insuranceService: InsuranceService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.insuranceService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'insuranceListModification',
        content: 'Deleted an insurance'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-insurance-delete-popup',
  template: ''
})
export class InsuranceDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ insurance }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(InsuranceDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.insurance = insurance;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/insurance', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/insurance', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
