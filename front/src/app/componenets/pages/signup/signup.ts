import { Component, inject, OnInit, ChangeDetectorRef, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { FormBuilder, Validators, ReactiveFormsModule, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';
import { ApiService } from '../../../services/api-service';
import { AuthService } from '../../../services/auth-service';
import { Campus } from '../../../models/campus.model';
import { Town } from '../../../models/town.model';

function passwordMatch(g: AbstractControl) {
  return g.get('password')?.value === g.get('rePassword')?.value ? null : { passwordsMismatch: true };
}

function strongPassword(control: AbstractControl): ValidationErrors | null {
  const v: string = control.value || '';
  const ok = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&?])[A-Za-z\d!@#$%^&?]{8,}$/.test(v);
  return ok ? null : { weakPassword: true };
}

@Component({
  selector: 'page-signup',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink, Header, Footer],
  templateUrl: './signup.html',
  styleUrl: './signup.css',
})
export class Signup implements OnInit {
  fb         = inject(FormBuilder);
  api        = inject(ApiService);
  auth       = inject(AuthService);
  router     = inject(Router);
  cdr        = inject(ChangeDetectorRef);
  platformId = inject(PLATFORM_ID);

  campuses: Campus[] = [];
  towns: Town[]      = [];
  loading            = false;
  error              = '';
  showPassword       = false;

  form = this.fb.group({
    username:           ['', [Validators.required, Validators.minLength(3), Validators.maxLength(16)]],
    email:              ['', [Validators.required, Validators.email]],
    name:               ['', Validators.required],
    birthdate:          ['', Validators.required],
    genre:              ['', Validators.required],
    phone:              ['', [Validators.required, Validators.pattern(/^\d{9}$/)]],
    idUsualCampus:      ['', Validators.required],
    idHomeTown:         ['', Validators.required],
    drivingLicenseYear: [null as number | null],
    description:        [''],
    password:           ['', [Validators.required, strongPassword]],
    rePassword:         ['', Validators.required],
  }, { validators: passwordMatch });

  ngOnInit(): void {
    if (!isPlatformBrowser(this.platformId)) return;
    this.api.getCampuses().subscribe({
      next: c => { this.campuses = c; this.cdr.detectChanges(); },
      error: e => console.error('[Signup] getCampuses error:', e),
    });
    this.api.getTowns().subscribe({
      next: t => { this.towns = t; this.cdr.detectChanges(); },
      error: e => console.error('[Signup] getTowns error:', e),
    });
  }

  submit() {
    if (this.form.invalid || this.loading) return;
    this.loading = true; this.error = '';
    const v = this.form.value;
    const dto = {
      username:           v.username,
      email:              v.email,
      name:               v.name,
      birthdate:          v.birthdate,
      genre:              v.genre,
      phone:              v.phone,
      idUsualCampus:      v.idUsualCampus ? Number(v.idUsualCampus) : null,
      idHomeTown:         v.idHomeTown    ? Number(v.idHomeTown)    : null,
      drivingLicenseYear: v.drivingLicenseYear || null,
      description:        v.description,
      password:           v.password,
    };
    this.api.register(dto).subscribe({
      next: () => { this.loading = false; this.router.navigate(['/login']); },
      error: e => {
        const fields = e?.error?.fields;
        this.error = fields
          ? 'Error: ' + Object.entries(fields).map(([k, v]) => `${k}: ${v}`).join(', ')
          : e?.error?.message || 'Error al registrarse.';
        this.loading = false;
        this.cdr.detectChanges();
      },
    });
  }
}
