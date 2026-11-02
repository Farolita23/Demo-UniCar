import { Routes } from '@angular/router';
// Solo la portada va en el paquete inicial; el resto de páginas se descargan al visitarlas
import { Home } from './components/pages/home/home';
import { NoAuthGuard } from './guards/no-auth-guard';
import { AuthGuard } from './guards/auth-guard';
import { NoBannedGuard } from './guards/no-banned-guard';

export const routes: Routes = [
    {
        path: '', 
        canActivateChild: [NoBannedGuard],
        children: [
            { path: '', component: Home, pathMatch: 'full' },
            { path: 'faq', loadComponent: () => import('./components/pages/faq/faq').then(m => m.Faq) },
            { path: 'about', loadComponent: () => import('./components/pages/about/about').then(m => m.About) },
            { path: 'trip-detail/:id', loadComponent: () => import('./components/pages/trip-detail/trip-detail').then(m => m.TripDetail) },
            { path: 'user/:id', loadComponent: () => import('./components/pages/user-profile/user-profile').then(m => m.UserProfile) },
            // Buscar viajes es público (solo lectura): pedir plaza o publicar sí piden cuenta
            { path: 'search-trip', loadComponent: () => import('./components/pages/search-trip/search-trip').then(m => m.SearchTrip) },
            {
                path: '',
                canActivate: [NoAuthGuard],
                children: [
                    { path: 'login', loadComponent: () => import('./components/pages/login/login').then(m => m.Login) },
                    { path: 'signup', loadComponent: () => import('./components/pages/signup/signup').then(m => m.Signup) },
                ],
            },
            {
                path: '',
                canActivate: [AuthGuard],
                children: [
                    { path: 'profile', loadComponent: () => import('./components/pages/profile/profile').then(m => m.Profile) },
                    { path: 'logout', loadComponent: () => import('./components/pages/logout/logout').then(m => m.Logout) },
                    { path: 'trip', loadComponent: () => import('./components/pages/trip/trip').then(m => m.PageTrip) },
                    { path: 'my-trips', loadComponent: () => import('./components/pages/my-trips/my-trips').then(m => m.MyTrips) },
                    { path: 'manage-trip/:id', loadComponent: () => import('./components/pages/manage-trip/manage-trip').then(m => m.ManageTrip) },
                    { path: 'favorites', loadComponent: () => import('./components/pages/favorites/favorites').then(m => m.Favorites) },
                    { path: 'admin', loadComponent: () => import('./components/pages/admin/admin').then(m => m.Admin) },
                ],
            },
            { path: '**', redirectTo: '' },
        ]
    }
];