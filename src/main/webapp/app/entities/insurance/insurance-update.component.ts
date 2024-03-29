import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IInsurance, Insurance } from 'app/shared/model/insurance.model';
import { InsuranceService } from './insurance.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { AccountService } from '../../core/auth/account.service';

@Component({
  selector: 'jhi-insurance-update',
  templateUrl: './insurance-update.component.html'
})
export class InsuranceUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
    coveragePercentage: [null, [Validators.required, Validators.min(0)]],
    startDate: [null, [Validators.required]],
    coveragePeriod: [null, [Validators.required, Validators.min(0)]],
    price: [null, [Validators.required, Validators.min(0)]],
    insuranceType: [null, [Validators.required]],
    riskType: [null, [Validators.required]],
    user: []
  });

  currentAccount: any;

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected insuranceService: InsuranceService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    protected accountService: AccountService
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ insurance }) => {
      this.updateForm(insurance);
    });
    this.accountService.identity().then(account => {
      this.currentAccount = account;
      if (this.isAdmin()) {
        this.getAllUsersInfo();
      } else {
        this.getUserInfo(this.currentAccount.login);
      }
    });
  }

  getAllUsersInfo() {
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  getUserInfo(login: string) {
    this.userService
      .find(login)
      .pipe(
        filter((mayBeOk: HttpResponse<IUser>) => mayBeOk.ok),
        map((response: HttpResponse<IUser>) => response.body)
      )
      .subscribe((res: IUser) => this.setUserData(res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  setUserData(user: IUser) {
    this.users = [user];
    this.editForm.get('user').setValue(this.users[0]);
    this.editForm.get('user').disable();
  }

  isAdmin(): boolean {
    return this.currentAccount ? this.currentAccount.authorities.includes('ROLE_ADMIN') : null;
  }

  updateForm(insurance: IInsurance) {
    this.editForm.patchValue({
      id: insurance.id,
      name: insurance.name,
      description: insurance.description,
      coveragePercentage: insurance.coveragePercentage * 100,
      startDate: insurance.startDate != null ? insurance.startDate.format(DATE_TIME_FORMAT) : null,
      coveragePeriod: insurance.coveragePeriod,
      price: insurance.price,
      insuranceType: insurance.insuranceType,
      riskType: insurance.riskType,
      user: insurance.user
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const insurance = this.createFromForm();
    if (insurance.id !== undefined) {
      this.subscribeToSaveResponse(this.insuranceService.update(insurance));
    } else {
      this.subscribeToSaveResponse(this.insuranceService.create(insurance));
    }
  }

  private createFromForm(): IInsurance {
    return {
      ...new Insurance(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      description: this.editForm.get(['description']).value,
      coveragePercentage: this.editForm.get(['coveragePercentage']).value / 100,
      startDate:
        this.editForm.get(['startDate']).value != null ? moment(this.editForm.get(['startDate']).value, DATE_TIME_FORMAT) : undefined,
      coveragePeriod: this.editForm.get(['coveragePeriod']).value,
      price: this.editForm.get(['price']).value,
      insuranceType: this.editForm.get(['insuranceType']).value,
      riskType: this.editForm.get(['riskType']).value,
      user: this.editForm.get(['user']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInsurance>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
