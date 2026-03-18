import { ChangeDetectorRef, Component } from '@angular/core';
import { AppConfig } from '../../services/app-config';
import { AbstractControl, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { NgIf } from '@angular/common';
import { ApiService } from '../../services/api-service';
import { AuthService } from '../../services/auth-service';
import { finalize } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
	selector: 'app-page-signup',
	imports: [
		NgIf,
        RouterLink,
		ReactiveFormsModule,
	],
	templateUrl: './page-signup.html',
	styleUrl: './page-signup.css',
})
export class PageSignup {
	
	constructor(
        public cd: ChangeDetectorRef,
		public appConfig: AppConfig,
		public api: ApiService,
		public auth: AuthService,
		private router: Router
	) { }
    

	form = new FormGroup(
        {
            "username": new FormControl('', { nonNullable: true, validators: [
                Validators.required, 
                Validators.minLength(3), 
                Validators.maxLength(16),
            ]}),
            "email": new FormControl('', { nonNullable: true, validators: [
                Validators.required, 
                Validators.email,
            ]}),
            "password": new FormControl('', { nonNullable: true, validators: [
                Validators.required,
                Validators.pattern(/(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}/)
            ]}),
            "rePassword": new FormControl('', { nonNullable: true, validators: [
                Validators.required, 
                Validators.pattern(/(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}/)
            ]}),
		}, { 
            validators: [
                ( form: AbstractControl) => {
                    const password = form.get('password')?.value;
                    const rePassword = form.get('rePassword')?.value;
                    return password === rePassword ? null : { passwordsMismatch: true };
                }
            ]
        }
	)


    loading = false;
    userExist = null;

	cancel() {
		this.router.navigate([this.appConfig.refer()])
	}


	submit() {
        if(this.loading) return

        this.userExist = null

        if (this.form.invalid) {
            this.form.markAllAsTouched();
            return;
        }

        this.loading = true;
		const {username, email, password} = this.form.getRawValue();
        this.api.authSignup(username, email, password)
        .pipe(finalize(()=> {
            this.loading = false;
            this.cd.detectChanges();
        }))
        .subscribe({ 
            next: (resp: any) => {
                this.auth.setToken(resp.token);
                this.router.navigate([this.appConfig.refer()])
            },
            error: (e: HttpErrorResponse) => {
                this.userExist = e.status === 409 ? e.error.error: null
            }
        })
    }
}
