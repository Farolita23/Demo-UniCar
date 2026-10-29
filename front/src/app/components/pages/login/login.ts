import { Component, inject, ChangeDetectorRef, AfterViewInit, ElementRef, ViewChild, PLATFORM_ID, NgZone } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';
import { AppIcon } from '../../elements/icon/icon';
import { ApiService } from '../../../services/api-service';
import { AuthService } from '../../../services/auth-service';
import { GoogleAuthService } from '../../../services/google-auth';

@Component({
    selector: 'page-login',
    standalone: true,
    imports: [ReactiveFormsModule, CommonModule, RouterLink, Header, AppIcon],
    templateUrl: './login.html',
    styleUrl: './login.css',
})
export class Login implements AfterViewInit {

    // Inyección de servicios
    private cdr = inject(ChangeDetectorRef);
    fb = inject(FormBuilder);
    api = inject(ApiService);
    auth = inject(AuthService);
    router = inject(Router);
    route = inject(ActivatedRoute);
    googleAuth = inject(GoogleAuthService);
    platformId = inject(PLATFORM_ID);
    private zone = inject(NgZone);

    // Contenedor donde Google dibuja su botón
    @ViewChild('googleBtn') googleBtn?: ElementRef<HTMLElement>;

    // Formulario de login con validaciones
    form = this.fb.group({
        username: ['', [Validators.required, Validators.minLength(3)]],
        password: ['', [Validators.required]],
    });

    // Estado de carga y error
    loading = false;
    error = '';

    // Control para mostrar la contraseña
    showPassword = false;

    // Control para evitar múltiples envíos del formulario
    submited = false;
    
    ngAfterViewInit(): void {
        if (!isPlatformBrowser(this.platformId) || !this.googleBtn) return;
        this.googleAuth.renderButton(this.googleBtn.nativeElement, idToken => this.zone.run(() => this.loginWithGoogle(idToken)));
    }

    // Inicia sesión con Google; si la cuenta no existe, lleva al registro con los datos prerrellenados
    private loginWithGoogle(idToken: string) {
        this.loading = true;
        this.error = '';
        this.api.googleLogin(idToken).subscribe({
            next: (res) => {
                if (res.needsRegistration) {
                    this.loading = false;
                    const google = { idToken, email: res.email, name: res.name, picture: res.picture };
                    // Copia en sessionStorage: sobrevive a recargas y a navegaciones sin state
                    try { sessionStorage.setItem('unicar_google_signup', JSON.stringify({ ...google, at: Date.now() })); } catch { }
                    this.router.navigate(['/signup'], { state: { google } });
                    return;
                }
                this.auth.saveToken(res.token!);
                this.api.getMe().subscribe({
                    next: (user) => { this.auth.saveUser(user); this.loading = false; this.router.navigate(['/']); },
                    error: () => { this.loading = false; this.router.navigate(['/']); }
                });
            },
            error: (e) => {
                this.error = e?.status === 403
                    ? 'Tu cuenta ha sido baneada. Recuerda cumplir con tus viajes pendientes. En caso de no hacerlo, el baneo podrá prolongarse.'
                    : 'No se pudo iniciar sesión con Google. Inténtalo de nuevo.';
                this.loading = false;
                this.cdr.detectChanges();
            }
        });
    }

    // Método para enviar el formulario de login
    submit() {
        this.submited = true;
        if (this.form.invalid || this.loading) return;
        this.loading = true;
        this.error = '';
        const { username, password } = this.form.value;
        this.api.login(username!, password!).subscribe({
            next: (res) => {
                this.auth.saveToken(res.token);
                // Obtener datos del usuario autenticado y guardarlos
                this.api.getMe().subscribe({
                    next: (user) => {
                        this.auth.saveUser(user);
                        this.loading = false;
                        this.router.navigate(["/"]);
                    },
                    error: () => {
                        // Token guardado aunque falle el /me — el perfil cargará igualmente
                        this.loading = false;
                        this.router.navigate(["/"]);
                    }
                });
            },
            error: (e) => {
                switch (e.error.status) {
                    case 403: this.error = 'Tu cuenta ha sido baneada. Recuerda cumplir con tus viajes pendientes. En caso de no hacerlo, el baneo podrá prolongarse.'; break;
                    default: this.error = 'Credenciales incorrectas. Inténtalo de nuevo.';
                }
                this.loading = false;
                this.cdr.detectChanges();
            }
        });
    }
}
