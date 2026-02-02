import { Routes } from '@angular/router';
import { PageHome } from './componenets/page-home/page-home';
import { PageLogin } from './componenets/page-login/page-login';
import { PageSignup } from './componenets/page-signup/page-signup';
import { PageFaq } from './componenets/page-faq/page-faq';
import { PageLogout } from './componenets/page-logout/page-logout';
import { NoAuthGuard } from './guards/no-auth-guard';
import { AuthGuard } from './guards/auth-guard';
import { PageProfile } from './componenets/page-profile/page-profile';
import { PageAbout } from './componenets/page-about/page-about';
import { PageRecoveryPassword } from './componenets/page-recovery-password/page-recovery-password';
import { PageResetPassword } from './componenets/page-reset-password/page-reset-password';
import { PageSearchTrip } from './componenets/page-search-trip/page-search-trip';

export const routes: Routes = [
    { path: '', component: PageHome },
    { path: 'faq', component: PageFaq },
    { path: 'about', component: PageAbout },
    { path: 'search-trip', component: PageSearchTrip },
    { path: '', canActivate: [NoAuthGuard], children: [
        { path: 'login', component: PageLogin },
        { path: 'recovery-password', component: PageRecoveryPassword },
        { path: 'reset-password', component: PageResetPassword },
        { path: 'signup', component: PageSignup },
    ]},
    { path: '', canActivate: [AuthGuard], children: [
        { path: 'profile/me', component: PageProfile },
        { path: 'logout', component: PageLogout },
    ]},
];