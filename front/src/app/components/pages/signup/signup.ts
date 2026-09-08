import { Component, inject, OnInit, ChangeDetectorRef, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { FormBuilder, Validators, ReactiveFormsModule, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Header } from '../../elements/header/header';
import { AppIcon } from '../../elements/icon/icon';
import { ApiService } from '../../../services/api-service';
import { AuthService } from '../../../services/auth-service';
import { Campus } from '../../../models/campus.model';
import { Town } from '../../../models/town.model';

// Función de validación personalizada para verificar que las contraseñas coincidan
function passwordMatch(g: AbstractControl) {
    const password = g.get('password')?.value;
    const rePassword = g.get('rePassword')?.value;

    return password === rePassword ? null : { passwordsMismatch: true };
}

// Función de validación personalizada para verificar la fortaleza de la contraseña
function strongPassword(control: AbstractControl): ValidationErrors | null {
    const v: string = control.value || '';

    const errors: ValidationErrors = {};

    if (v.length < 8         ) errors['minlength'] = { requiredLength: 8, actualLength: v.length };
    if (!/[a-z]/.test(v)     ) errors['lowercase'] = true;
    if (!/[A-Z]/.test(v)     ) errors['uppercase'] = true;
    if (!/\d/.test(v)        ) errors['number'] = true;
    if (!/[!@#$%^&?]/.test(v)) errors['special'] = true;

    return Object.keys(errors).length ? errors : null;
}

@Component({
    selector: 'page-signup',
    standalone: true,
    imports: [ReactiveFormsModule, CommonModule, RouterLink, Header, AppIcon],
    templateUrl: './signup.html',
    styleUrl: './signup.css',
})
export class Signup implements OnInit {
    
    // Inyección de servicios
    fb = inject(FormBuilder);
    api = inject(ApiService);
    auth = inject(AuthService);
    router = inject(Router);
    cdr = inject(ChangeDetectorRef);
    platformId = inject(PLATFORM_ID);

    // Variables para almacenar los campus y pueblos obtenidos de la API
    campuses: Campus[] = [];
    towns: Town[] = [];

    // Variable para indicar si se está procesando el registro
    loading = false;

    // Variable para mostrar mensajes de error en el formulario
    error = '';

    // Variable para controlar la visibilidad de la contraseña en el formulario
    showPassword = false;

    // Formulario de registro con validaciones
    form = this.fb.group({
        username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(16)]],
        email: ['', [Validators.required, Validators.email]],
        name: ['', Validators.required],
        birthdate: ['', Validators.required],
        genre: ['', Validators.required],
        phone: ['', [Validators.required, Validators.pattern(/^\d{9}$/)]],
        idUsualCampus: ['', Validators.required],
        idHomeTown: ['', Validators.required],
        drivingLicenseYear: [null as number | null],
        description: [''],
        password: ['', [Validators.required, strongPassword]],
        rePassword: ['', Validators.required],
    }, { validators: passwordMatch });

    // Método para inicializar el componente y cargar los datos necesarios
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

    // Variable para controlar si el formulario ha sido enviado
    submited = false;

    // Método para manejar el envío del formulario de registro
    submit() {
        this.submited = true;

        if (this.form.invalid || this.loading) return;

        this.loading = true; this.error = '';
        const v = this.form.value;
        const dto = {
            username: v.username,
            email: v.email,
            name: v.name,
            birthdate: v.birthdate,
            genre: v.genre,
            phone: v.phone,
            idUsualCampus: v.idUsualCampus ? Number(v.idUsualCampus) : null,
            idHomeTown: v.idHomeTown ? Number(v.idHomeTown) : null,
            drivingLicenseYear: v.drivingLicenseYear || null,
            description: v.description,
            password: v.password,
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
