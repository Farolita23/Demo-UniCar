import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { AuthService } from '../services/auth-service';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
    constructor(
        private router: Router,
        private auth: AuthService,
    ) { }

    async canActivate(): Promise<boolean> {
        console.log(this.auth.isLoggedIn());
        
        if(this.auth.isLoggedIn()){
            console.log("LOGGED");
        }else{
            console.log("NOT LOGGED");
        }
        return true
    }
}
