import { Component, inject, HostListener, OnInit, ChangeDetectorRef, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { RouterLink, RouterLinkActive, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AppConfig } from '../../../services/app-config';
import { AuthService } from '../../../services/auth-service';
import { ApiService } from '../../../services/api-service';
import { User } from '../../../models/user.model';
import { Subject, debounceTime, switchMap, of } from 'rxjs';

/**
 * Componente de encabezado global de la aplicación.
 * Maneja la navegación principal, búsqueda de usuarios en tiempo real y notificaciones.
 */
@Component({
    selector: 'app-header',
    standalone: true,
    imports: [RouterLink, RouterLinkActive, CommonModule, FormsModule],
    templateUrl: './header.html',
    styleUrl: './header.css',
})
export class Header implements OnInit {

    // Inyección de dependencias mediante la función inject
    config = inject(AppConfig);
    auth = inject(AuthService);
    api = inject(ApiService);
    router = inject(Router);
    cdr = inject(ChangeDetectorRef);
    platformId = inject(PLATFORM_ID);

    // Estados de control de la interfaz de usuario
    showNav = false;
    showDropdown = false;

    // Propiedades para la funcionalidad de búsqueda de personas
    searchQuery = '';
    searchResults: User[] = [];
    showSearchResults = false;
    searchLoading = false;
    
    // Sujeto para manejar el flujo de entrada de búsqueda con operadores reactivos
    private search$ = new Subject<string>();

    ngOnInit(): void {
        /**
         * Configuración del pipeline de búsqueda:
         * 1. debounceTime: Espera 300ms tras la última pulsación para evitar peticiones excesivas.
         * 2. switchMap: Cancela la petición anterior si entra una nueva búsqueda.
         */
        this.search$.pipe(
            debounceTime(300),
            switchMap(q => {
                if (!q.trim()) return of([]);
                this.searchLoading = true;
                return this.api.searchUsers(q.trim());
            })
        ).subscribe({
            next: results => {
                console.log(results);
                this.searchResults = results;
                this.searchLoading = false;
                this.showSearchResults = this.searchQuery.trim().length > 0;
                // Forzar detección de cambios ante actualizaciones asíncronas
                this.cdr.detectChanges();
            },
            error: () => {
                this.searchLoading = false;
                this.cdr.detectChanges();
            },
        });

    }

    /** Getter para verificar si el usuario actual posee rol de administrador */
    get isAdmin(): boolean {
        return this.auth.getUser()?.role === 'ADMIN';
    }

    /**
     * Emite el nuevo valor de búsqueda al stream reactivo.
     */
    onSearchInput() {
        this.search$.next(this.searchQuery);
        if (this.searchQuery.trim() === "") {
            this.showSearchResults = false;
            this.searchResults = [];
        } else {
            this.showSearchResults = true;
            this.searchLoading = true;
        }
    }

    /**
     * Navega al perfil del usuario seleccionado. 
     * Redirige a '/profile' si es el usuario propio o a '/user/id' si es un tercero.
     * @param user Objeto de usuario seleccionado en la búsqueda.
     */
    goToUserProfile(user: User) {
        this.showSearchResults = false;
        this.searchQuery = '';
        this.searchResults = [];
        const myId = this.auth.getUser()?.id;
        
        if (user.id === myId) {
            this.router.navigate(['/profile']);
        } else {
            this.router.navigate(['/user', user.id]);
        }
    }

    // Cierra el panel de resultados de búsqueda
    closeSearchResults() {
        this.showSearchResults = false;
    }

    // Alterna la visibilidad del menú de navegación móvil 
    alternateNav() { this.showNav = !this.showNav; }

    // Alterna la visibilidad del menú desplegable de usuario
    toggleDropdown() { this.showDropdown = !this.showDropdown; }

    /**
     * Escucha clics globales en el documento para cerrar menús desplegables 
     * cuando el usuario hace clic fuera de ellos (Click-outside pattern).
     */
    @HostListener('document:click', ['$event'])
    onDocumentClick(event: MouseEvent) {
        const target = event.target as HTMLElement;
        
        // Cerrar dropdown de perfil si el clic es fuera de su contenedor
        if (!target.closest('.profile-dropdown-wrap')) {
            this.showDropdown = false;
        }
        
        // Cerrar resultados de búsqueda si el clic es fuera de su contenedor
        if (!target.closest('.search-wrap')) {
            this.showSearchResults = false;
        }
    }
}