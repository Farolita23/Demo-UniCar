import { ChangeDetectorRef, Component } from '@angular/core';
import { AuthService } from '../../services/auth-service';
import { ApiService } from '../../services/api-service';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AppConfig } from '../../services/app-config';
import { Router, RouterLink } from '@angular/router';
import { NgIf } from '@angular/common';
import { finalize } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
	selector: 'app-page-login',
	imports: [
		NgIf,
        RouterLink,
		ReactiveFormsModule,
	],
	templateUrl: './page-login.html',
	styleUrl: './page-login.css',
})
export class PageLogin {

	constructor(
		private cd: ChangeDetectorRef,
		private router: Router,
		public appConfig: AppConfig,
		private api: ApiService, 
		private auth: AuthService
	) { }


	form = new FormGroup({
		"username": new FormControl<string>('', { nonNullable: true, validators: [
			Validators.required, 
			Validators.minLength(3), 
			Validators.maxLength(16),
		]}),
		"password": new FormControl<string>('', { nonNullable: true, validators: [
			Validators.required,
            Validators.pattern(/(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}/)
		]}),
	})


    loading = false;
    invalidCredentials = false
	

	cancel() {
		this.router.navigate([this.appConfig.refer()])
	}

	
	submit() {
        if(this.loading) return

        this.invalidCredentials = false;

        if (this.form.invalid) {
            this.form.markAllAsTouched();
            return;
        }

        this.loading = true;
		const {username, password} = this.form.getRawValue();
		this.api.authLogin(username, password)
		.pipe(finalize(() => {
			this.loading = false;
			this.cd.detectChanges();
		}))
		.subscribe({ 
			next: (resp: any) => { 
				this.auth.setToken(resp.token);
				this.router.navigate([this.appConfig.refer()])
			}, 
			error: (e: HttpErrorResponse) => {
				this.invalidCredentials = e.status === 401;
            }
        });
	}

}
