import { UserSummary } from './user.model';

export interface Car {
  id: number;
  model: string;
  color: string;
  licensePlate: string;
  capacity: number;
  driverDTO: UserSummary;
}
