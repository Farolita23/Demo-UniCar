import { Component, inject, OnInit, ChangeDetectorRef, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { FormBuilder, Validators, ReactiveFormsModule, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { GoogleSignupState } from '../../../services/google-auth';
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

    // Registro con Google: datos de la cuenta recibidos desde la página de login
    googleState: GoogleSignupState | null = null;
    get googleMode(): boolean { return !!this.googleState; }

    // Registro con Google por fases: controles que se validan en cada paso
    readonly googleSteps = [
        { title: 'Elige tu usuario',       hint: 'Así te verán el resto de usuarios.',                          controls: ['username'] },
        { title: 'Sobre ti',               hint: 'Tu fecha de nacimiento y tu género.',                         controls: ['birthdate', 'genre'] },
        { title: 'Tu teléfono',            hint: 'Para que tus compañeros de viaje puedan contactarte.',        controls: ['phone'] },
        { title: '¿Dónde te mueves?',      hint: 'Tu campus habitual y tu localidad de origen.',                controls: ['idUsualCampus', 'idHomeTown'] },
        { title: 'Último paso (opcional)', hint: 'Si conduces, cuéntanos desde cuándo. Puedes dejarlo vacío.',  controls: ['drivingLicenseYear', 'description'] },
    ];
    step = 0;

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
        const state = history.state as { google?: GoogleSignupState } | null;
        if (state?.google) {
            this.googleState = state.google;
            // Sin contraseña propia: el acceso es con Google
            (this.form as any).removeControl('password');
            (this.form as any).removeControl('rePassword');
            this.form.patchValue({ email: state.google.email, name: state.google.name });
        }
        this.api.getCampuses().subscribe({
            next: c => { this.campuses = c; this.cdr.detectChanges(); },
            error: e => console.error('[Signup] getCampuses error:', e),
        });
        this.api.getTowns().subscribe({
            next: t => { this.towns = t; this.cdr.detectChanges(); },
            error: e => console.error('[Signup] getTowns error:', e),
        });
    }

    // Avanza de fase si los campos de la actual son válidos; en la última, crea la cuenta
    nextStep() {
        const controls = this.googleSteps[this.step].controls.map(c => this.form.get(c)!);
        controls.forEach(c => c.markAsTouched());
        if (controls.some(c => c.invalid)) return;
        if (this.step < this.googleSteps.length - 1) { this.step++; return; }
        this.submit();
    }

    prevStep() {
        if (this.step > 0) this.step--;
    }

    // Variable para controlar si el formulario ha sido enviado
    submited = false;

    // Método para manejar el envío del formulario de registro
    submit() {
        this.submited = true;

        if (this.form.invalid || this.loading) {
            // En el registro por fases, volver al primer paso con errores
            const first = this.googleSteps.findIndex(st => st.controls.some(c => this.form.get(c)?.invalid));
            if (this.googleMode && first >= 0) this.step = first;
            return;
        }

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
        if (this.googleState) {
            const { email, password, ...profile } = dto as any;
            this.api.googleRegister({ ...profile, idToken: this.googleState.idToken }).subscribe({
                next: (res) => {
                    this.auth.saveToken(res.token);
                    this.api.getMe().subscribe({
                        next: (user) => { this.auth.saveUser(user); this.loading = false; this.router.navigate(['/']); },
                        error: () => { this.loading = false; this.router.navigate(['/']); },
                    });
                },
                error: e => {
                    const fields = e?.error?.fields;
                    this.error = fields
                        ? 'Error: ' + Object.entries(fields).map(([k, v]) => `${k}: ${v}`).join(', ')
                        : e?.error?.message || 'Error al registrarse con Google.';
                    // Usuario repetido: volver al paso del usuario
                    if (e?.status === 409) this.step = 0;
                    this.loading = false;
                    this.cdr.detectChanges();
                },
            });
            return;
        }
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
