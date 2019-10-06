import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Insurance } from 'app/shared/model/insurance.model';
import { InsuranceService } from './insurance.service';
import { InsuranceComponent } from './insurance.component';
import { InsuranceDetailComponent } from './insurance-detail.component';
import { InsuranceUpdateComponent } from './insurance-update.component';
import { InsuranceDeletePopupComponent } from './insurance-delete-dialog.component';
import { IInsurance } from 'app/shared/model/insurance.model';

@Injectable({ providedIn: 'root' })
export class InsuranceResolve implements Resolve<IInsurance> {
  constructor(private service: InsuranceService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IInsurance> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Insurance>) => response.ok),
        map((insurance: HttpResponse<Insurance>) => insurance.body)
      );
    }
    return of(new Insurance());
  }
}

export const insuranceRoute: Routes = [
  {
    path: '',
    component: InsuranceComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'insuranceApp.insurance.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: InsuranceDetailComponent,
    resolve: {
      insurance: InsuranceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'insuranceApp.insurance.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: InsuranceUpdateComponent,
    resolve: {
      insurance: InsuranceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'insuranceApp.insurance.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: InsuranceUpdateComponent,
    resolve: {
      insurance: InsuranceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'insuranceApp.insurance.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const insurancePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: InsuranceDeletePopupComponent,
    resolve: {
      insurance: InsuranceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'insuranceApp.insurance.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
