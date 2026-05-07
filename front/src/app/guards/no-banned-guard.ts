import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { ApiService } from '../services/api-service';
import { Router } from '@angular/router';
import { catchError, map, of } from 'rxjs';
import { AuthService } from '../services/auth-service';

export const NoBannedGuard: CanActivateFn = () => {
    const router = inject(Router);
    const apiService = inject(ApiService);
    const auth = inject(AuthService);
    
    if(!auth.isLoggedIn()) return true;

    return apiService.getMe().pipe(
        map((res: any) => {
            if (res.banned) {
                alert("Tu cuenta ha sido baneada. Contacta con soporte para más información.");
                auth.logout();
                router.navigate(['/']);
                return false;
            }
            return true;
        }),
        catchError((err) => {
            console.error(err);
            return of(true);
        })
    );
};