/**
 * Componente de Página de Administración
 * 
 * Gestiona la administración de usuarios y reportes en la aplicación.
 * Incluye funcionalidades para buscar usuarios, banear/desbanear, añadir strikes
 * y gestionar reportes de usuarios.
 */

// Importaciones de Angular core
import { Component, inject, OnInit, ChangeDetectorRef, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';

// Importaciones de componentes compartidos
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';
import { AppIcon } from '../../elements/icon/icon';

// Importaciones de servicios
import { ApiService, UserListItem } from '../../../services/api-service';
import { AuthService } from '../../../services/auth-service';

// Importaciones de modelos
import { Report } from '../../../models/user.model';
import { AvatarUrlPipe } from '../../../pipes/avatar-url.pipe';

/**
 * Componente Admin
 * 
 * Página principal de administración con tabs para usuarios y reportes.
 * Requiere autenticación de administrador para acceder.
 */
@Component({
    // Selector CSS para usar el componente en templates
    selector: 'page-admin',
    // Componente standalone sin necesidad de módulo
    standalone: true,
    // Módulos y componentes importados
    imports: [AvatarUrlPipe, CommonModule, FormsModule, RouterLink, Header, Footer, AppIcon],
    // Archivo HTML de la plantilla
    templateUrl: './admin.html',
    // Archivo CSS de estilos
    styleUrl: './admin.css',
})
export class Admin implements OnInit {
    // Inyección de servicios
    api = inject(ApiService);
    auth = inject(AuthService);
    router = inject(Router);
    cdr = inject(ChangeDetectorRef);
    platformId = inject(PLATFORM_ID);

    /** Tab activo: 'users' para lista de usuarios o 'reports' para reportes */
    activeTab: 'users' | 'reports' = 'users';
    
    /** Indicador de estado de carga */
    loading = true;

    // ========================================
    // Propiedades relacionadas con usuarios
    // ========================================
    
    /** Lista de usuarios filtrados por búsqueda */
    filteredUsers: UserListItem[] = [];
    
    /** Número total de usuarios (para paginación) */
    totalUsers = 0;
    
    /** Número total de páginas */
    totalUserPages = 0;

    /** Página actual */
    currentUserPage = 0;

    /** Texto de búsqueda ingresado por el usuario */
    userSearch = '';

    // ========================================
    // Propiedades relacionadas con reportes
    // ========================================
    
    /** Lista de reportes de usuarios */
    reports: Report[] = [];

    /** Número total de reportes (para paginación) */
    totalReports = 0;

    /** Número total de páginas para reportes */
    totalReportPages = 0;

    /** Página actual para reportes */
    currentReportPage = 0;

    // ========================================
    // Propiedades de detalle de usuario
    // ========================================
    
    /** Usuario actualmente seleccionado para ver detalles */
    selectedUser: UserListItem | null = null;

    // ========================================
    // Propiedades de retroalimentación de acciones
    // ========================================
    
    /** Mensaje de éxito de acción a mostrar */
    actionSuccess = '';
    
    /** Mensaje de error de acción a mostrar */
    actionError = '';

    /**
     * Hook de ciclo de vida de Angular
     * Se ejecuta después de que Angular ha inicializado todas las propiedades enlazadas de la directiva
     */
    ngOnInit(): void {
        // Verificar si estamos en el navegador (no en servidor)
        if (!isPlatformBrowser(this.platformId)) { this.loading = false; return; }
        
        // Obtener usuario autenticado
        const me = this.auth.getUser();
        
        // Validar que el usuario existe y tiene rol de admin
        if (!me || me.role !== 'ADMIN') {
            this.router.navigate(['/']);
            return;
        }
        
        // Cargar datos iniciales
        this.loadData();
    }

    /**
     * Carga los datos iniciales (usuarios y reportes) desde el servidor
     */
    loadData() {
        // Mostrar indicador de carga
        this.loading = true;
        // Cargar lista de usuarios
        this.searchUsers();
        // Cargar lista de reportes
        this.searchReports();
    }

    /**
     * Filtra usuarios según el texto de búsqueda ingresado
     * Si no hay búsqueda, muestra todos los usuarios
     */
    searchUsers(page = 0) {
        // El servidor devuelve siempre una página (máx. 100); sin texto equivale al listado paginado
        this.api.adminSearchUsers(this.userSearch.trim() || "", page).subscribe({
            next: (pageable) => {
                this.filteredUsers = pageable.content; 
                this.totalUsers = pageable.totalElements;
                this.totalUserPages = pageable.totalPages;
                this.currentUserPage = pageable.number;
                this.loading = false;
                this.cdr.detectChanges(); 
            },
            error: () => { this.loading = false; this.cdr.detectChanges(); },
        });
    }

    /**
     * Carga la lista de reportes de usuarios desde el servidor
     */
    searchReports(page = 0) {
        this.api.adminSearchReports(page).subscribe({
            next: (pageable) => {
                this.reports = pageable.content; 
                this.totalReports = pageable.totalElements;
                this.totalReportPages = pageable.totalPages;
                this.currentReportPage = pageable.number;
                this.loading = false;
                this.cdr.detectChanges(); 
            },
            error: () => { this.loading = false; this.cdr.detectChanges(); },
        });
    }

    /**
     * Selecciona un usuario para mostrar sus detalles
     * @param u - Usuario a seleccionar
     */
    selectUser(u: UserListItem) {
        this.selectedUser = u;
    }

    /**
     * Navega al usuario reportado y muestra sus detalles
     * @param userId - ID del usuario reportado
     */
    viewReportedUser(userId: number) {
        this.activeTab = 'users';
        this.api.getUser(userId).subscribe({
            next: u => { this.selectUser(u); this.cdr.detectChanges(); },
        });
    }

    /**
     * Cierra el panel de detalle de usuario
     */
    closeUserDetail() { this.selectedUser = null; }

    /**
     * Banea un usuario por su ID
     * @param id - ID del usuario a banear
     */
    banUser(id: number) {
        this.api.adminBanUser(id).subscribe({
            next: () => {
                this.showAction('Usuario baneado.');
                if (this.selectedUser?.id === id) this.selectedUser!.banned = true;
                this.cdr.detectChanges();
            },
            error: () => this.showActionError('Error al banear usuario.'),
        });
    }

    /**
     * Desbanea un usuario por su ID
     * @param id - ID del usuario a desbanear
     */
    unbanUser(id: number) {
        this.api.adminUnbanUser(id).subscribe({
            next: () => {
                this.showAction('Usuario desbaneado.');
                if (this.selectedUser?.id === id) this.selectedUser!.banned = false;
                this.cdr.detectChanges();
            },
            error: () => this.showActionError('Error al desbanear.'),
        });
    }

    /**
     * Añade un strike (advertencia) a un usuario
     * @param id - ID del usuario al que añadir el strike
     */
    addStrike(id: number) {
        this.api.adminAddStrike(id).subscribe({
            next: () => {
                this.showAction('Strike añadido.');
                if(this.selectedUser) this.selectedUser.strikes++; 
                this.cdr.detectChanges();
            },
            error: () => this.showActionError('Error al añadir strike.'),
        });
    }

    /**
     * Elimina un reporte de usuario por su ID
     * @param id - ID del reporte a eliminar
     */
    deleteReport(id: number) {
        this.api.adminDeleteReport(id).subscribe({
            next: () => {
                this.reports = this.reports.filter(r => r.id !== id);
                this.showAction('Reporte eliminado.');
                this.cdr.detectChanges();
            },
            error: () => this.showActionError('Error al eliminar reporte.'),
        });
    }

    /**
     * Muestra un mensaje de éxito de acción
     * Se oculta automáticamente después de 4 segundos
     * @private
     * @param msg - Mensaje a mostrar
     */
    private showAction(msg: string) {
        this.actionSuccess = msg;
        setTimeout(() => { this.actionSuccess = ''; this.cdr.detectChanges(); }, 4000);
    }

    /**
     * Muestra un mensaje de error de acción
     * Se oculta automáticamente después de 4 segundos
     * @private
     * @param msg - Mensaje de error a mostrar
     */
    private showActionError(msg: string) {
        this.actionError = msg;
        setTimeout(() => { this.actionError = ''; this.cdr.detectChanges(); }, 4000);
    }

    /**
     * Obtiene un array con los números de página para la paginación
     * @returns Array de números de página
     */
    userPages(): number[] { return this.pageWindow(this.currentUserPage, this.totalUserPages); }

    /**
     * Ventana de números de página alrededor de la actual (máx. 5), para no pintar miles de botones
     */
    private pageWindow(current: number, total: number): number[] {
        const start = Math.max(0, Math.min(current - 2, total - 5));
        const end = Math.min(total, start + 5);
        return Array.from({ length: end - start }, (_, i) => start + i);
    }

    /**
     * Obtiene un array con los números de página para la paginación de reportes
     * @returns Array de números de página para reportes
     */
    reportPages(): number[] { return Array.from({ length: this.totalReportPages }, (_, i) => i); }

}
