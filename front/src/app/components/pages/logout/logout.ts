/* Importación de módulos necesarios de Angular */
import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth-service';

/* Componente de logout (página sin UI) */
@Component({
    selector: 'page-logout',
    standalone: true,
    imports: [],
    template: ''
})

/* Clase del componente Logout */
export class Logout implements OnInit {

    /* Inyección del servicio de autenticación */
    auth = inject(AuthService);

    /* Inyección del router para redirecciones */
    router = inject(Router);

    /* Hook de inicialización */
    ngOnInit() {

        /* Ejecuta el logout del usuario */
        this.auth.logout();

        /* Redirige al inicio tras cerrar sesión */
        this.router.navigate(['/']);
    }
}