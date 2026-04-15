import { Component, inject, OnInit, ChangeDetectorRef, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';
import { ApiService } from '../../../services/api-service';
import { AuthService } from '../../../services/auth-service';
import { Trip } from '../../../models/trip.model';

@Component({
  selector: 'page-my-trips',
  standalone: true,
  imports: [CommonModule, RouterLink, Header, Footer],
  templateUrl: './my-trips.html',
  styleUrl: './my-trips.css',
})
export class MyTrips implements OnInit {
  api        = inject(ApiService);
  auth       = inject(AuthService);
  router     = inject(Router);
  cdr        = inject(ChangeDetectorRef);
  platformId = inject(PLATFORM_ID);

  tripsAsPassenger: Trip[] = [];
  tripsAsDriver: Trip[]    = [];
  loadingPassenger = true;
  loadingDriver    = true;
  activeTab: 'passenger' | 'driver' = 'passenger';

  ngOnInit(): void {
    if (!isPlatformBrowser(this.platformId)) {
      this.loadingPassenger = false;
      this.loadingDriver    = false;
      return;
    }
    const userId = this.auth.getUser()?.id;
    if (!userId) { this.router.navigate(['/login']); return; }

    this.api.getTripsAsPassenger(userId).subscribe({
      next: p => { this.tripsAsPassenger = p.content; this.loadingPassenger = false; this.cdr.detectChanges(); },
      error: () => { this.loadingPassenger = false; this.cdr.detectChanges(); },
    });
    this.api.getTripsAsDriver(userId).subscribe({
      next: p => { this.tripsAsDriver = p.content; this.loadingDriver = false; this.cdr.detectChanges(); },
      error: () => { this.loadingDriver = false; this.cdr.detectChanges(); },
    });
  }

  goToDetail(id: number) { this.router.navigate(['/trip-detail', id]); }

  goToManage(id: number) { this.router.navigate(['/manage-trip', id]); }

  freeSeats(trip: Trip): number {
    return (trip.carDTO?.capacity ?? 0) - (trip.passengersDTO?.length ?? 0);
  }

  isFull(trip: Trip): boolean {
    return this.freeSeats(trip) <= 0;
  }

  formatDate(d: string): string {
    if (!d) return '';
    return new Date(d).toLocaleDateString('es-ES', { weekday: 'short', day: 'numeric', month: 'short', year: 'numeric' });
  }
}
