import { Component, inject, OnInit, ChangeDetectorRef, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';
import { TripCard } from '../../elements/trip/trip';
import { ApiService } from '../../../services/api-service';
import { AuthService } from '../../../services/auth-service';
import { Favorite } from '../../../models/user.model';
import { Trip } from '../../../models/trip.model';

@Component({
  selector: 'page-favorites',
  standalone: true,
  imports: [CommonModule, RouterLink, Header, Footer, TripCard],
  templateUrl: './favorites.html',
  styleUrl: './favorites.css',
})
export class Favorites implements OnInit {
  api        = inject(ApiService);
  auth       = inject(AuthService);
  router     = inject(Router);
  cdr        = inject(ChangeDetectorRef);
  platformId = inject(PLATFORM_ID);

  favorites: Favorite[] = [];
  favoriteTrips: Trip[]  = [];
  loading     = true;
  loadingTrips = true;
  activeTab: 'people' | 'trips' = 'people';

  ngOnInit(): void {
    if (!isPlatformBrowser(this.platformId)) { this.loading = false; return; }
    const userId = this.auth.getUser()?.id;
    if (!userId) { this.loading = false; return; }

    this.api.getFavorites(userId).subscribe({
      next: favs => {
        this.favorites = favs;
        this.loading = false;
        this.loadFavoriteTrips(favs);
        this.cdr.detectChanges();
      },
      error: () => { this.loading = false; this.cdr.detectChanges(); },
    });
  }

  private loadFavoriteTrips(favs: Favorite[]) {
    if (favs.length === 0) { this.loadingTrips = false; return; }

    // For each favorite user who is a driver, get their trips
    let pending = favs.length;
    const allTrips: Trip[] = [];

    for (const fav of favs) {
      this.api.getTripsAsDriver(fav.favoriteUserDTO.id).subscribe({
        next: page => {
          const raw = page as any;
          const trips = raw.content ?? [];
          allTrips.push(...trips);
          pending--;
          if (pending === 0) {
            // Sort by date desc and dedupe
            this.favoriteTrips = allTrips
              .sort((a, b) => new Date(b.departureDate).getTime() - new Date(a.departureDate).getTime());
            this.loadingTrips = false;
            this.cdr.detectChanges();
          }
        },
        error: () => {
          pending--;
          if (pending === 0) {
            this.favoriteTrips = allTrips;
            this.loadingTrips = false;
            this.cdr.detectChanges();
          }
        },
      });
    }
  }

  removeFavorite(fav: Favorite) {
    const userId = this.auth.getUser()?.id;
    if (!userId) return;
    this.api.removeFavorite(userId, fav.favoriteUserDTO.id).subscribe({
      next: () => {
        this.favorites = this.favorites.filter(f => f.id !== fav.id);
        this.cdr.detectChanges();
      },
    });
  }

  goToProfile(id: number) {
    this.router.navigate(['/user', id]);
  }

  updateTrip(updated: Trip, i: number) { this.favoriteTrips[i] = updated; }
}
