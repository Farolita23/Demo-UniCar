import { Routes } from '@angular/router';
import { Home } from './componenets/pages/home/home';
import { Faq } from './componenets/pages/faq/faq';
import { About } from './componenets/pages/about/about';
import { Profile } from './componenets/pages/profile/profile';
import { SearchTrip } from './componenets/pages/search-trip/search-trip';
import { Signup } from './componenets/pages/signup/signup';
import { Login } from './componenets/pages/login/login';
import { Logout } from './componenets/pages/logout/logout';
import { RecoveryPassword } from './componenets/pages/recovery-password/recovery-password';
import { ResetPassword } from './componenets/pages/reset-password/reset-password';
import { Settings } from './componenets/pages/settings/settings';
import { PageTrip } from './componenets/pages/trip/trip';
import { TripDetail } from './componenets/pages/trip-detail/trip-detail';
import { MyTrips } from './componenets/pages/my-trips/my-trips';
import { NoAuthGuard } from './guards/no-auth-guard';
import { AuthGuard } from './guards/auth-guard';

export const routes: Routes = [
  { path: '', component: Home, pathMatch: 'full' },
  { path: 'faq', component: Faq },
  { path: 'about', component: About },
  { path: 'search-trip', component: SearchTrip },
  { path: 'trip-detail/:id', component: TripDetail },
  {
    path: '',
    canActivate: [NoAuthGuard],
    children: [
      { path: 'login', component: Login },
      { path: 'recovery-password', component: RecoveryPassword },
      { path: 'reset-password', component: ResetPassword },
      { path: 'signup', component: Signup },
    ],
  },
  {
    path: '',
    canActivate: [AuthGuard],
    children: [
      { path: 'profile', component: Profile },
      { path: 'logout', component: Logout },
      { path: 'trip', component: PageTrip },
      { path: 'settings', component: Settings },
      { path: 'my-trips', component: MyTrips },
    ],
  },
  { path: '**', redirectTo: '' },
];