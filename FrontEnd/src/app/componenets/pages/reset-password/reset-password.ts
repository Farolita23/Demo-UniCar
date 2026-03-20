import { ChangeDetectorRef, Component } from '@angular/core';
import { AppConfig } from '../../../services/app-config';
import { ApiService } from '../../../services/api-service';
import { AuthService } from '../../../services/auth-service';
import { Router, RouterLink } from '@angular/router';
import { AbstractControl, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Location, NgIf } from '@angular/common';
import { finalize } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-reset-password',
  imports: [
    NgIf,
    RouterLink,
    ReactiveFormsModule,
  ],
  templateUrl: './reset-password.html',
  styleUrl: './reset-password.css',
})
export class ResetPassword {	
	constructor(
        public cd: ChangeDetectorRef,
		public appConfig: AppConfig,
        public location: Location,
		public api: ApiService,
		public auth: AuthService,
		private router: Router
	) { }
    

	form = new FormGroup({
        "password": new FormControl('', { nonNullable: true, validators: [
            Validators.required,
            Validators.pattern(/(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}/)
        ]}),
        "rePassword": new FormControl('', { nonNullable: true, validators: [
            Validators.required, 
            Validators.pattern(/(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}/)
        ]}),
		}, { validators: [
            ( form: AbstractControl) => {
                const password = form.get('password')?.value;
                const rePassword = form.get('rePassword')?.value;
                return password === rePassword ? null : { passwordsMismatch: true };
            }
        ]}
	)


    loading = false;
    responseSuccess = false;
    responseError: any = null;

	cancel() {
		this.router.navigate([this.appConfig.refer()])
	}


	submit() {
        if(this.loading) return

        this.responseSuccess = false
        this.responseError = null;

        if (this.form.invalid) {
            this.form.markAllAsTouched();
            return;
        }

        this.loading = true;
		const {password} = this.form.getRawValue();
        const params = new URLSearchParams(this.location.path(true).split('?')[1]);
        const token = params.get('token') || '';

        this.api.authResetPassword(token, password)
        .pipe(finalize(()=> {
            this.loading = false;
            this.cd.detectChanges();
        }))
        .subscribe({ 
            next: (resp: any) => {
                this.responseSuccess = true;
                this.responseError = null;
            },
            error: (e: HttpErrorResponse) => {
                this.responseSuccess = false;
                this.responseError = e.error.error
            }
        })
    }
}
