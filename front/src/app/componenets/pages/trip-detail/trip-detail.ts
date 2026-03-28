import { Component, inject, afterNextRender } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';
import { ApiService } from '../../../services/api-service';
import { AuthService } from '../../../services/auth-service';
import { Trip } from '../../../models/trip.model';
import { User } from '../../../models/user.model';

@Component({
  selector: 'page-trip-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, Header, Footer],
  templateUrl: './trip-detail.html',
  styleUrl: './trip-detail.css',
})
export class TripDetail {
  api   = inject(ApiService);
  auth  = inject(AuthService);
  route = inject(ActivatedRoute);

  trip: Trip | null   = null;
  driver: User | null = null;
  loading       = true;
  actionLoading = false;
  actionError   = '';

  constructor() {
    afterNextRender(() => {
      const id = Number(this.route.snapshot.paramMap.get('id'));
      if (!id) { this.loading = false; return; }

      this.api.getTripById(id).subscribe({
        next: (t) => {
          this.trip   = t;
          this.loading = false;
          const driverId = t.carDTO?.driverDTO?.id;
          if (driverId) {
            this.api.getUser(driverId).subscribe({
              next: (u) => this.driver = u,
              error: () => {}
            });
          }
        },
        error: () => { this.loading = false; }
      });
    });
  }

  get freeSeats(): number {
    return (this.trip?.carDTO?.capacity ?? 0) - (this.trip?.passengersDTO?.length ?? 0);
  }

  get currentUserId(): number | null {
    return this.auth.getUser()?.id ?? null;
  }

  get isPassenger(): boolean {
    return this.trip?.passengersDTO?.some(p => p.id === this.currentUserId) ?? false;
  }

  get hasRequested(): boolean {
    return this.trip?.requestersDTO?.some(r => r.id === this.currentUserId) ?? false;
  }

  get avgRating(): number {
    const ratings = this.driver?.ratingsReceivedDTO;
    if (!ratings || ratings.length === 0) return 0;
    return ratings.reduce((s, r) => s + r.rating, 0) / ratings.length;
  }

  formatDate(d: string): string {
    if (!d) return '';
    return new Date(d).toLocaleDateString('es-ES', { weekday: 'long', day: 'numeric', month: 'long', year: 'numeric' });
  }

  requestJoin() {
    if (!this.currentUserId || this.actionLoading || !this.trip) return;
    this.actionLoading = true; this.actionError = '';
    this.api.requestJoinTrip(this.trip.id, this.currentUserId).subscribe({
      next: (t) => { this.trip = t; this.actionLoading = false; },
      error: (e) => { this.actionError = e?.error?.message || 'Error al solicitar plaza.'; this.actionLoading = false; }
    });
  }

  cancelRequest() {
    if (!this.currentUserId || this.actionLoading || !this.trip) return;
    this.actionLoading = true; this.actionError = '';
    this.api.cancelJoinRequest(this.trip.id, this.currentUserId).subscribe({
      next: (t) => { this.trip = t; this.actionLoading = false; },
      error: (e) => { this.actionError = e?.error?.message || 'Error al cancelar.'; this.actionLoading = false; }
    });
  }

  leaveTrip() {
    if (!this.currentUserId || this.actionLoading || !this.trip) return;
    this.actionLoading = true; this.actionError = '';
    this.api.leaveTrip(this.trip.id, this.currentUserId).subscribe({
      next: (t) => { this.trip = t; this.actionLoading = false; },
      error: (e) => { this.actionError = e?.error?.message || 'Error al abandonar.'; this.actionLoading = false; }
    });
  }
}
