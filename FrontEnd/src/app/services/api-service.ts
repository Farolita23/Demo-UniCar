import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from './auth-service';
import { env } from '../../environments/env';

@Injectable({ providedIn: 'root' })
export class ApiService {
    
    readonly URL = env.API_URL;
    
    constructor(
		private http: HttpClient,
		private authService: AuthService
	) { }
    
	#post(path: string, body?: any, headers?: HttpHeaders) {
		headers = headers || new HttpHeaders();
		const token = this.authService.getToken();
		if (token) {
			headers = headers.append("Authorization", "Bearer " + token);
		}
        return this.http.post(this.URL+path, body, { headers });
	}
    auth = {
        login: (username: string, password: string) => {
		    return this.#post(`/api/auth/login`, { username: username, password: password })
        }
    }

    authSignup(username: string, email: string, password: string) {
		return this.#post(`/signup`, { username: username, email: email, password: password })
	}
    
    authResetPassword(token: string, password: string) {
        return this.#post("/reset-password", { token: token, password: password})
    }
    authRecoveryPassword(email: string) {
        return this.#post(`/recovery-password`, { email: email })
    }
    
}
