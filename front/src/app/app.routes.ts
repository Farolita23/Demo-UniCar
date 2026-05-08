import { Routes } from '@angular/router';
import { Home } from './components/pages/home/home';
import { Faq } from './components/pages/faq/faq';
import { About } from './components/pages/about/about';
import { Profile } from './components/pages/profile/profile';
import { SearchTrip } from './components/pages/search-trip/search-trip';
import { Signup } from './components/pages/signup/signup';
import { Login } from './components/pages/login/login';
import { Logout } from './components/pages/logout/logout';
import { PageTrip } from './components/pages/trip/trip';
import { TripDetail } from './components/pages/trip-detail/trip-detail';
import { MyTrips } from './components/pages/my-trips/my-trips';
import { ManageTrip } from './components/pages/manage-trip/manage-trip';
import { UserProfile } from './components/pages/user-profile/user-profile';
import { Favorites } from './components/pages/favorites/favorites';
import { Admin } from './components/pages/admin/admin';
import { NoAuthGuard } from './guards/no-auth-guard';
import { AuthGuard } from './guards/auth-guard';
import { NoBannedGuard } from './guards/no-banned-guard';

export const routes: Routes = [
    {
        path: '', 
        canActivateChild: [NoBannedGuard],
        children: [
            { path: '', component: Home, pathMatch: 'full' },
            { path: 'faq', component: Faq },
            { path: 'about', component: About },
            { path: 'trip-detail/:id', component: TripDetail },
            { path: 'user/:id', component: UserProfile },
            {
                path: '',
                canActivate: [NoAuthGuard],
                children: [
                    { path: 'login', component: Login },
                    { path: 'signup', component: Signup },
                ],
            },
            {
                path: '',
                canActivate: [AuthGuard],
                children: [
                    { path: 'search-trip', component: SearchTrip },
                    { path: 'profile', component: Profile },
                    { path: 'logout', component: Logout },
                    { path: 'trip', component: PageTrip },
                    { path: 'my-trips', component: MyTrips },
                    { path: 'manage-trip/:id', component: ManageTrip },
                    { path: 'favorites', component: Favorites },
                    { path: 'admin', component: Admin },
                ],
            },
            { path: '**', redirectTo: '' },
        ]
    }
];