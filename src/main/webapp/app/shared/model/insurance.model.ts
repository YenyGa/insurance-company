import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { InsuranceType } from 'app/shared/model/enumerations/insurance-type.model';
import { RiskType } from 'app/shared/model/enumerations/risk-type.model';

export interface IInsurance {
  id?: number;
  name?: string;
  description?: string;
  coveragePercentage?: number;
  startDate?: Moment;
  coveragePeriod?: number;
  price?: number;
  insuranceType?: InsuranceType;
  riskType?: RiskType;
  user?: IUser;
}

export class Insurance implements IInsurance {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public coveragePercentage?: number,
    public startDate?: Moment,
    public coveragePeriod?: number,
    public price?: number,
    public insuranceType?: InsuranceType,
    public riskType?: RiskType,
    public user?: IUser
  ) {}
}
