import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { ApiService } from '../services/api-service';
import { Router } from '@angular/router';
import { catchError, map, of } from 'rxjs';
import { AuthService } from '../services/auth-service';

export const NoBannedGuard: CanActivateFn = () => {
    const router = inject(Router);
    const auth = inject(AuthService);
    const apiService = inject(ApiService);
    
    if(!auth.isLoggedIn()) return true;

    return apiService.getMe().pipe(
        map((res: any) => {
            if (res.banned) {
                alert("Tu cuenta ha sido baneada.");
                auth.logout();
                router.navigate(['/']);
                return false;
            }
            return true;
        }),
        catchError((err) => {
            if(err.status === 403){
                alert("El usuario actual no ha sido encontrado. Porfavor, vuelva a iniciar sesion");
                auth.logout();
                router.navigate(["/"])
                return of(true);
            }
            return of(true);
        })
    );
};