import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';

@Injectable({ providedIn: 'root' })
export class NoAuthGuard implements CanActivate {
    constructor(
        private router: Router,
        private cookie: CookieService,
    ) { }

    async canActivate(): Promise<boolean> {
        if(this.cookie.check("token")){
            this.router.navigate(["/"])
            return false
        }
        return true;
    }
}
