import { Car } from './car.model';
import { Campus } from './campus.model';
import { Town } from './town.model';
import { UserSummary } from './user.model';

export interface Trip {
  id: number;
  carDTO: Car;
  campusDTO: Campus;
  townDTO: Town;
  driverDTO?: UserSummary;
  isToCampus: boolean;
  departureDate: string;
  departureTime: string;
  departureAddress: string;
  price: number;
  passengersDTO: UserSummary[];
  requestersDTO: UserSummary[];
  /** Id de la plantilla periódica que generó el viaje; null/undefined si es puntual. */
  periodicTripId?: number | null;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}
