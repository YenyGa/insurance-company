import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IInsurance } from 'app/shared/model/insurance.model';
import { AccountService } from 'app/core/auth/account.service';
import { InsuranceService } from './insurance.service';

@Component({
  selector: 'jhi-insurance',
  templateUrl: './insurance.component.html'
})
export class InsuranceComponent implements OnInit, OnDestroy {
  insurances: IInsurance[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected insuranceService: InsuranceService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.insuranceService
      .query()
      .pipe(
        filter((res: HttpResponse<IInsurance[]>) => res.ok),
        map((res: HttpResponse<IInsurance[]>) => res.body)
      )
      .subscribe(
        (res: IInsurance[]) => {
          this.insurances = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  loadByUser(userId: number) {
    this.insuranceService
      .queryByUser(userId)
      .pipe(
        filter((res: HttpResponse<IInsurance[]>) => res.ok),
        map((res: HttpResponse<IInsurance[]>) => res.body)
      )
      .subscribe(
        (res: IInsurance[]) => {
          this.insurances = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.accountService.identity().then(account => {
      this.currentAccount = account;
      if (this.isAdmin()) {
        this.loadAll();
      } else {
        this.loadByUser(this.currentAccount.id);
      }
    });

    this.registerChangeInInsurances();
  }

  isAdmin(): boolean {
    return this.currentAccount ? this.currentAccount.authorities.includes('ROLE_ADMIN') : null;
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  registerChangeInInsurances() {
    this.eventSubscriber = this.eventManager.subscribe('insuranceListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
