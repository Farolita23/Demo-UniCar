import { Campus } from './campus.model';
import { Town } from './town.model';

export interface Rating {
  id: number;
  rating: number;
  userRateDTO?: UserSummary;
  ratedUserDTO?: UserSummary;
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
  usualCampusDTO: Campus | null;
  homeTownDTO: Town | null;
  description?: string;
  profileImageUrl?: string;
  ratingsReceivedDTO: Rating[] | null;
  ratingsDoneDTO: Rating[] | null;
}
