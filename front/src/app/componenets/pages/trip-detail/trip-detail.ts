import { Component, inject, OnInit, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';
import { AppIcon } from '../../elements/icon/icon';
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
  platformId = inject(PLATFORM_ID);

  trip: Trip | null = null;
  driver: User | null = null;
  loading      = true;
  actionLoading = false;
  actionError   = '';

  ngOnInit() {
    if (!isPlatformBrowser(this.platformId)) return;
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!id) { this.loading = false; return; }

    this.api.getTripById(id).subscribe({
      next: (t) => {
        this.trip = t;
        this.loading = false;
        // Load driver info from car owner — use passengers/requesters to infer driver
        // The driver is whoever owns the car; we try to get from the trip's car owner
        // Since API doesn't expose driver directly we try getUser with the car's owner
        // For now we'll show info we have; if driverDTO is added to Trip model it can be used
        this.loadDriver(t);
      },
      error: () => { this.loading = false; }
    });
  }

  loadDriver(trip: Trip) {
    // Try to find driver: the backend Trip model should expose a driverDTO or driverID
    // Since current model only has carDTO, campusDTO, townDTO, passengersDTO, requestersDTO
    // we'll try to call the user endpoint if the carDTO has an owner id
    // For now fall back to showing partial info from carDTO
    // If the backend returns a driverDTO in the future, update here
    const driverIdFromCar = (trip.carDTO as any)?.driverDTO?.id || (trip.carDTO as any)?.ownerId;
    if (driverIdFromCar) {
      this.api.getUser(driverIdFromCar).subscribe({
        next: (u) => this.driver = u,
        error: () => {}
      });
    }
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
