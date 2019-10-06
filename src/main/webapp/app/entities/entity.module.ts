import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'insurance',
        loadChildren: () => import('./insurance/insurance.module').then(m => m.InsuranceInsuranceModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class InsuranceEntityModule {}
