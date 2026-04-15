import { Component, Input, Output, EventEmitter, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { Trip } from '../../../models/trip.model';
import { AuthService } from '../../../services/auth-service';
import { ApiService } from '../../../services/api-service';
import { AppIcon } from '../icon/icon';

@Component({
  selector: 'app-trip',
  standalone: true,
  imports: [CommonModule, RouterLink, AppIcon],
  templateUrl: './trip.html',
  styleUrl: './trip.css',
})
export class TripCard {
  @Input() data!: Trip;
  @Output() tripUpdated = new EventEmitter<Trip>();

  auth = inject(AuthService);
  api = inject(ApiService);
  router = inject(Router);
  loading = false;

  get freeSeats(): number {
    return (this.data.carDTO?.capacity ?? 0) - (this.data.passengersDTO?.length ?? 0);
  }

  get direction(): string {
    return this.data.isToCampus ? 'Hacia campus' : 'Desde campus';
  }

  get currentUserId(): number | null {
    return this.auth.getUser()?.id ?? null;
  }

  get isPassenger(): boolean {
    return this.data.passengersDTO?.some(p => p.id === this.currentUserId) ?? false;
  }

  get hasRequested(): boolean {
    return this.data.requestersDTO?.some(r => r.id === this.currentUserId) ?? false;
  }

  get avgRating(): number {
    // placeholder
    return 4.5;
  }

  formatDate(d: string): string {
    if (!d) return '';
    const date = new Date(d);
    return date.toLocaleDateString('es-ES', { weekday: 'short', day: 'numeric', month: 'short' });
  }

  goToDetail() { this.router.navigate(['/trip-detail', this.data.id]); }

  requestJoin() {
    if (!this.currentUserId || this.loading) return;
    this.loading = true;
    this.api.requestJoinTrip(this.data.id, this.currentUserId).subscribe({
      next: (t) => { this.tripUpdated.emit(t); this.loading = false; },
      error: () => { this.loading = false; }
    });
  }

  cancelRequest() {
    if (!this.currentUserId || this.loading) return;
    this.loading = true;
    this.api.cancelJoinRequest(this.data.id, this.currentUserId).subscribe({
      next: (t) => { this.tripUpdated.emit(t); this.loading = false; },
      error: () => { this.loading = false; }
    });
  }

  leaveTrip() {
    if (!this.currentUserId || this.loading) return;
    this.loading = true;
    this.api.leaveTrip(this.data.id, this.currentUserId).subscribe({
      next: (t) => { this.tripUpdated.emit(t); this.loading = false; },
      error: () => { this.loading = false; }
    });
  }
}
