import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInsurance } from 'app/shared/model/insurance.model';

@Component({
  selector: 'jhi-insurance-detail',
  templateUrl: './insurance-detail.component.html'
})
export class InsuranceDetailComponent implements OnInit {
  insurance: IInsurance;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ insurance }) => {
      this.insurance = insurance;
    });
  }

  previousState() {
    window.history.back();
  }
}
