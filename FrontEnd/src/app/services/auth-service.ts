import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
@Injectable({ providedIn: 'root' })
export class AuthService {
    
    constructor(
        private cookie: CookieService
    ) {}

	isLoggedIn(): boolean {
		return (!!this.getToken());
	}
	
    removeToken() {
        this.cookie.delete("token")
	}
    
    setToken(token: any){
		const expires = new Date(Date.now() + 7 * 24 * 60 * 60 * 1000);
        this.cookie.set("token", token, expires, "/", undefined, false, 'Strict')
    }
    
    getToken(){
        return this.cookie.get("token");
    }

}