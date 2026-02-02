import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component, NgModule } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { finalize } from 'rxjs';
import { AppConfig } from '../../services/app-config';
import { ApiService } from '../../services/api-service';
import { AuthService } from '../../services/auth-service';
import { Router } from '@angular/router';
import { NgIf } from '@angular/common';

@Component({
    selector: 'app-page-recovery-password',
    imports: [
		NgIf,
		ReactiveFormsModule,
    ],
    templateUrl: './page-recovery-password.html',
    styleUrl: './page-recovery-password.css',
})
export class PageRecoveryPassword {

    constructor(
        private cd: ChangeDetectorRef,
        private router: Router,
        public appConfig: AppConfig,
        private api: ApiService,
        private auth: AuthService,
    ) { }


    form = new FormGroup({
        "email": new FormControl('', { nonNullable: true, validators: [
            Validators.required, 
            Validators.email,
        ]})
    })


    loading = false;
    isPreviousSend = false;
    responseSuccess = false;
    responseError: any = null;


    cancel() {
        this.router.navigate([this.appConfig.refer()])
    }

    submit() {
        if (this.loading) return
        
        this.responseSuccess = false;
        this.responseError = null;
        
        if (this.form.invalid) {
            this.form.markAllAsTouched();
            return;
        }
        
        this.loading = true;
        const { email } = this.form.getRawValue();
        this.api.authRecoveryPassword(email)
        .pipe(finalize(() => {
            this.loading = false;
            this.isPreviousSend = true;
            this.cd.detectChanges();
        }))
        .subscribe({
            next: (resp: any) => {
                // console.log(resp)
                this.responseSuccess = true;
                this.responseError = null;
            },
            error: (e: HttpErrorResponse) => {
                console.log(e);
                this.responseSuccess = false;
                this.responseError = e.error.error;                    
            }
        });
    }

}
