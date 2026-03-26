import { Campus } from './campus.model';
import { Town } from './town.model';
import { Car } from './car.model';

export interface Rating {
  id: number;
  rating: number;
}

export interface UserSummary {
  id: number;
  username: string;
  name: string;
  profileImageUrl?: string;
}

export interface User {
  id: number;
  username: string;
  email: string;
  name: string;
  birthdate: string;
  genre: string;
  phone: string;
  strikes: number;
  banned: boolean;
  drivingLicenseYear?: number;
  usualCampusDTO: Campus;
  homeTownDTO: Town;
  carsDTO: Car[];
  description?: string;
  profileImageUrl?: string;
  ratingsReceivedDTO: Rating[];
  ratingsDoneDTO: Rating[];
}
