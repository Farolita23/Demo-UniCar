import { Component, inject, afterNextRender, ChangeDetectorRef } from '@angular/core';
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
export class MyTrips {
  api    = inject(ApiService);
  auth   = inject(AuthService);
  router = inject(Router);
  cdr    = inject(ChangeDetectorRef);

  tripsAsPassenger: Trip[] = [];
  tripsAsDriver: Trip[]    = [];
  loadingPassenger = true;
  loadingDriver    = true;
  activeTab: 'passenger' | 'driver' = 'passenger';

  constructor() {
    afterNextRender(() => {
      const userId = this.auth.getUser()?.id;
      if (!userId) { this.router.navigate(['/login']); return; }

      this.api.getTripsAsPassenger(userId).subscribe({
        next: (p) => { this.tripsAsPassenger = p.content; this.loadingPassenger = false; this.cdr.detectChanges(); },
        error: ()  => { this.loadingPassenger = false; this.cdr.detectChanges(); },
      });

      this.api.getTripsAsDriver(userId).subscribe({
        next: (p) => { this.tripsAsDriver = p.content; this.loadingDriver = false; this.cdr.detectChanges(); },
        error: ()  => { this.loadingDriver = false; this.cdr.detectChanges(); },
      });
    });
  }

  goToDetail(id: number) { this.router.navigate(['/trip-detail', id]); }

  formatDate(d: string): string {
    if (!d) return '';
    return new Date(d).toLocaleDateString('es-ES', { weekday: 'short', day: 'numeric', month: 'short', year: 'numeric' });
  }
}
