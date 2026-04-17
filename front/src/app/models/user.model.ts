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
  role: string;
  usualCampusDTO: Campus | null;
  homeTownDTO: Town | null;
  description?: string;
  profileImageUrl?: string;
  ratingsReceivedDTO: Rating[] | null;
  ratingsDoneDTO: Rating[] | null;
}

export interface Favorite {
  id: number;
  userDTO: UserSummary;
  favoriteUserDTO: UserSummary;
}

export interface Warning {
  id: number;
  subject: string;
  message: string;
  createdAt: string;
  isRead: boolean;
  userDTO?: UserSummary;
  adminDTO?: UserSummary;
}

export interface Report {
  id: number;
  reason: string;
  date: string;
  userReportDTO?: UserSummary;
  reportedUserDTO?: UserSummary;
}
