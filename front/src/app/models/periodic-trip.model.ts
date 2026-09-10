import { Car } from './car.model';
import { Campus } from './campus.model';
import { Town } from './town.model';

export type DayOfWeek =
  | 'MONDAY'
  | 'TUESDAY'
  | 'WEDNESDAY'
  | 'THURSDAY'
  | 'FRIDAY'
  | 'SATURDAY'
  | 'SUNDAY';

/** Orden de los días para renderizar selectores y etiquetas. */
export const WEEK_DAYS: { value: DayOfWeek; short: string; label: string }[] = [
  { value: 'MONDAY', short: 'L', label: 'Lunes' },
  { value: 'TUESDAY', short: 'M', label: 'Martes' },
  { value: 'WEDNESDAY', short: 'X', label: 'Miércoles' },
  { value: 'THURSDAY', short: 'J', label: 'Jueves' },
  { value: 'FRIDAY', short: 'V', label: 'Viernes' },
  { value: 'SATURDAY', short: 'S', label: 'Sábado' },
  { value: 'SUNDAY', short: 'D', label: 'Domingo' },
];

export interface PeriodicTrip {
  id: number;
  carDTO: Car;
  campusDTO: Campus;
  townDTO: Town;
  isToCampus: boolean;
  departureAddress: string;
  price: number;
  startDate: string;
  endDate: string;
  departureTime: string;
  daysOfWeek: DayOfWeek[];
  repeatIntervalWeeks: number;
  tripsGenerated: number;
}

export interface PeriodicTripCreate {
  idCar: number;
  idCampus: number;
  idTown: number;
  isToCampus: boolean;
  departureAddress: string;
  price: number;
  startDate: string;
  endDate: string;
  departureTime: string;
  daysOfWeek: DayOfWeek[];
  repeatIntervalWeeks: number;
}
