import { Component, inject, OnInit, ChangeDetectorRef, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';
import { ApiService } from '../../../services/api-service';
import { AuthService } from '../../../services/auth-service';
import { User, Report } from '../../../models/user.model';

@Component({
    selector: 'page-admin',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterLink, Header, Footer],
    templateUrl: './admin.html',
    styleUrl: './admin.css',
})
export class Admin implements OnInit {
    api = inject(ApiService);
    auth = inject(AuthService);
    router = inject(Router);
    cdr = inject(ChangeDetectorRef);
    platformId = inject(PLATFORM_ID);

    activeTab: 'users' | 'reports' = 'users';
    loading = true;

    // Users
    users: User[] = [];
    filteredUsers: User[] = [];
    userSearch = '';

    // Reports
    reports: Report[] = [];

    // User detail
    selectedUser: User | null = null;

    // Action feedback
    actionSuccess = '';
    actionError = '';

    ngOnInit(): void {
        if (!isPlatformBrowser(this.platformId)) { this.loading = false; return; }
        const me = this.auth.getUser();
        if (!me || me.role !== 'ADMIN') {
            this.router.navigate(['/']);
            return;
        }
        this.loadData();
    }

    loadData() {
        this.loading = true;
        this.api.adminGetAllUsers().subscribe({
            next: u => {
                this.users = u;
                this.filteredUsers = u;
                this.loading = false;
                this.cdr.detectChanges();
            },
            error: () => { this.loading = false; this.cdr.detectChanges(); },
        });
        this.api.adminGetAllReports().subscribe({
            next: r => { this.reports = r; this.cdr.detectChanges(); },
        });
    }

    searchUsers() {
        if (!this.userSearch.trim()) {
            this.filteredUsers = this.users;
        } else {
            this.api.adminSearchUsers(this.userSearch.trim()).subscribe({
                next: u => { this.filteredUsers = u; this.cdr.detectChanges(); },
            });
        }
    }

    selectUser(u: User) {
        this.selectedUser = u;
    }

    viewReportedUser(userId: number) {
        this.activeTab = 'users';
        this.api.getUser(userId).subscribe({
            next: u => { this.selectUser(u); this.cdr.detectChanges(); },
        });
    }

    closeUserDetail() { this.selectedUser = null; }

    banUser(id: number) {
        this.api.adminBanUser(id).subscribe({
            next: () => {
                this.showAction('Usuario baneado.');
                this.loadData();
                if (this.selectedUser?.id === id) this.selectedUser!.banned = true;
                this.cdr.detectChanges();
            },
            error: () => this.showActionError('Error al banear usuario.'),
        });
    }

    unbanUser(id: number) {
        this.api.adminUnbanUser(id).subscribe({
            next: () => {
                this.showAction('Usuario desbaneado.');
                this.loadData();
                if (this.selectedUser?.id === id) this.selectedUser!.banned = false;
                this.cdr.detectChanges();
            },
            error: () => this.showActionError('Error al desbanear.'),
        });
    }

    addStrike(id: number) {
        this.api.adminAddStrike(id).subscribe({
            next: () => {
                this.showAction('Strike añadido.');
                this.loadData();
                this.cdr.detectChanges();
            },
            error: () => this.showActionError('Error al añadir strike.'),
        });
    }

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

    private showAction(msg: string) {
        this.actionSuccess = msg;
        setTimeout(() => { this.actionSuccess = ''; this.cdr.detectChanges(); }, 4000);
    }
    private showActionError(msg: string) {
        this.actionError = msg;
        setTimeout(() => { this.actionError = ''; this.cdr.detectChanges(); }, 4000);
    }
}
