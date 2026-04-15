import { Component, inject, OnInit, ChangeDetectorRef, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
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
export class TripDetail implements OnInit {
  api        = inject(ApiService);
  auth       = inject(AuthService);
  route      = inject(ActivatedRoute);
  cdr        = inject(ChangeDetectorRef);
  platformId = inject(PLATFORM_ID);

  trip: Trip | null   = null;
  driver: User | null = null;
  loading       = true;
  actionLoading = false;
  actionError   = '';

  ngOnInit(): void {
    if (!isPlatformBrowser(this.platformId)) { this.loading = false; return; }

    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!id) { this.loading = false; return; }

    this.api.getTripById(id).subscribe({
      next: t => {
        this.trip    = t;
        this.loading = false;
        this.cdr.detectChanges();
        // Obtener conductor vía car-owner endpoint (carDTO ya no tiene driverDTO)
        if (t.carDTO?.id) {
          this.loadDriver(t.carDTO.id);
        }
      },
      error: () => { this.loading = false; this.cdr.detectChanges(); },
    });
  }

  private loadDriver(carId: number) {
    // GET /api/user/car-owner/{carId} devuelve el UserDTO del conductor
    this.api.getCarOwner(carId).subscribe({
      next: u => { this.driver = u; this.cdr.detectChanges(); },
      error: e => console.error('[TripDetail] getCarOwner error:', e),
    });
  }

  get freeSeats(): number {
    return (this.trip?.carDTO?.capacity ?? 0) - (this.trip?.passengersDTO?.length ?? 0);
  }

  get isFull(): boolean {
    return this.freeSeats <= 0;
  }

  get currentUserId(): number | null { return this.auth.getUser()?.id ?? null; }

  get isDriver(): boolean {
    return !!this.driver && this.driver.id === this.currentUserId;
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
      next: t => { this.trip = t; this.actionLoading = false; this.cdr.detectChanges(); },
      error: e => { this.actionError = e?.error?.message || 'Error al solicitar plaza.'; this.actionLoading = false; this.cdr.detectChanges(); },
    });
  }

  cancelRequest() {
    if (!this.currentUserId || this.actionLoading || !this.trip) return;
    this.actionLoading = true; this.actionError = '';
    this.api.cancelJoinRequest(this.trip.id, this.currentUserId).subscribe({
      next: t => { this.trip = t; this.actionLoading = false; this.cdr.detectChanges(); },
      error: e => { this.actionError = e?.error?.message || 'Error al cancelar.'; this.actionLoading = false; this.cdr.detectChanges(); },
    });
  }

  leaveTrip() {
    if (!this.currentUserId || this.actionLoading || !this.trip) return;
    this.actionLoading = true; this.actionError = '';
    this.api.leaveTrip(this.trip.id, this.currentUserId).subscribe({
      next: t => { this.trip = t; this.actionLoading = false; this.cdr.detectChanges(); },
      error: e => { this.actionError = e?.error?.message || 'Error al abandonar.'; this.actionLoading = false; this.cdr.detectChanges(); },
    });
  }
}
