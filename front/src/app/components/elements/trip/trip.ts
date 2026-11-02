import { Component, Input, Output, EventEmitter, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { Trip } from '../../../models/trip.model';
import { AuthService } from '../../../services/auth-service';
import { ApiService } from '../../../services/api-service';
import { AppIcon } from '../icon/icon';
import { AvatarUrlPipe } from '../../../pipes/avatar-url.pipe';

@Component({
  selector: 'app-trip',
  standalone: true,
  imports: [CommonModule, RouterLink, AppIcon, AvatarUrlPipe],
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

  /** "1 plaza libre" / "3 plazas libres" */
  get seatsLabel(): string {
    return this.freeSeats === 1 ? '1 plaza libre' : `${this.freeSeats} plazas libres`;
  }

  /** Hora sin segundos: "07:50" */
  get timeLabel(): string {
    return (this.data.departureTime || '').slice(0, 5);
  }

  /** Precio en formato español: "2,20 €" */
  get priceLabel(): string {
    return Number(this.data.price ?? 0).toLocaleString('es-ES', { style: 'currency', currency: 'EUR' });
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

  get driverId(): number | null {
    return this.data.driverDTO?.id ?? null;
  }

  get isOwnTrip(): boolean {
    return this.driverId === this.currentUserId;
  }

  /** Nombre corto del conductor: "Lucía M." (primera palabra + inicial de la última) */
  get driverShortName(): string {
    const parts = (this.data.driverDTO?.name || '').trim().split(/\s+/).filter(Boolean);
    if (parts.length === 0) return this.data.driverDTO?.username ?? '';
    if (parts.length === 1) return parts[0];
    return `${parts[0]} ${parts[parts.length - 1][0].toUpperCase()}.`;
  }

  /** Iniciales para el avatar cuando no hay foto */
  get driverInitials(): string {
    const parts = (this.data.driverDTO?.name || this.data.driverDTO?.username || '?').trim().split(/\s+/);
    return ((parts[0]?.[0] ?? '') + (parts.length > 1 ? parts[parts.length - 1][0] : '')).toUpperCase();
  }

  get ratingsCount(): number {
    return this.data.driverDTO?.ratingsCount ?? 0;
  }

  /** Media con un decimal y coma: "4,8" */
  get ratingLabel(): string {
    const avg = this.data.driverDTO?.avgRating;
    return avg == null ? '' : avg.toLocaleString('es-ES', { minimumFractionDigits: 1, maximumFractionDigits: 1 });
  }

  get ratingsCountLabel(): string {
    return this.ratingsCount === 1 ? '1 valoración' : `${this.ratingsCount} valoraciones`;
  }

  /** Nombre accesible de la fila del conductor */
  get driverAriaLabel(): string {
    const rating = this.ratingsCount > 0
      ? `valoración ${this.ratingLabel} de 5, ${this.ratingsCountLabel}`
      : 'nuevo en UniCar, sin valoraciones';
    return `Ver perfil de ${this.driverShortName}, ${rating}`;
  }

  formatDate(d: string): string {
    if (!d) return '';
    const date = new Date(d);
    return date.toLocaleDateString('es-ES', { weekday: 'short', day: 'numeric', month: 'short' });
  }

  goToDetail() { this.router.navigate(['/trip-detail', this.data.id]); }
  goToManage() { this.router.navigate(['/manage-trip', this.data.id]); }

  goToDriverProfile(event: Event) {
    event.stopPropagation();
    if (this.driverId) this.router.navigate(['/user', this.driverId]);
  }

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
