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

@Component({
    selector: 'app-header',
    standalone: true,
    imports: [RouterLink, RouterLinkActive, CommonModule, FormsModule],
    templateUrl: './header.html',
    styleUrl: './header.css',
})
export class Header implements OnInit {
    config = inject(AppConfig);
    auth = inject(AuthService);
    api = inject(ApiService);
    router = inject(Router);
    cdr = inject(ChangeDetectorRef);
    platformId = inject(PLATFORM_ID);

    showNav = false;
    showDropdown = false;

    // Person search
    searchQuery = '';
    searchResults: User[] = [];
    showSearchResults = false;
    searchLoading = false;
    private search$ = new Subject<string>();

    // Unread warnings count
    unreadWarnings = 0;

    ngOnInit(): void {
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
                this.cdr.detectChanges();
            },
            error: () => {
                this.searchLoading = false;
                this.cdr.detectChanges();
            },
        });

        if (isPlatformBrowser(this.platformId) && this.auth.isLoggedIn()) {
            this.loadUnreadWarnings();
        }
    }

    loadUnreadWarnings() {
        const userId = this.auth.getUser()?.id;
        if (!userId) return;
        this.api.getUnreadWarningCount(userId).subscribe({
            next: r => { this.unreadWarnings = r.count; this.cdr.detectChanges(); },
        });
    }

    get isAdmin(): boolean {
        return this.auth.getUser()?.role === 'ADMIN';
    }

    onSearchInput() {
        this.search$.next(this.searchQuery);
        if (this.searchQuery.trim() === "") {
            this.showSearchResults = false;
            this.searchResults = [];
        }else{
            this.showSearchResults = true;
            this.searchLoading = true;
        }
    }

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

    closeSearchResults() {
        this.showSearchResults = false;
    }

    alternateNav() { this.showNav = !this.showNav; }
    toggleDropdown() { this.showDropdown = !this.showDropdown; }

    @HostListener('document:click', ['$event'])
    onDocumentClick(event: MouseEvent) {
        const target = event.target as HTMLElement;
        if (!target.closest('.profile-dropdown-wrap')) {
            this.showDropdown = false;
        }
        if (!target.closest('.search-wrap')) {
            this.showSearchResults = false;
        }
    }
}
