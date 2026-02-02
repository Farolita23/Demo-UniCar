import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from './auth-service';

@Injectable({ providedIn: 'root' })
export class ApiService {
    
    readonly URL = "http://KD-52666694Y:3000";
	
    constructor(
		private http: HttpClient,
		private auth: AuthService
	) { }

	#post(path: string, body?: any, headers?: HttpHeaders) {
		headers = headers || new HttpHeaders();
		const token = this.auth.getToken();
		if (token) {
			headers = headers.append("Authorization", "Bearer " + token);
		}
        return this.http.post(this.URL+path, body, { headers });
	}

    authLogin(username: string, password: string) {
		return this.#post(`/login`, { username: username, password: password })
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
