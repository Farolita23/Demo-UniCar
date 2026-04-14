import { Component, inject, OnInit, ChangeDetectorRef, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';
import { ApiService } from '../../../services/api-service';
import { AuthService } from '../../../services/auth-service';
import { Trip } from '../../../models/trip.model';
import { User, UserSummary } from '../../../models/user.model';

@Component({
  selector: 'page-manage-trip',
  standalone: true,
  imports: [CommonModule, RouterLink, Header, Footer],
  templateUrl: './manage-trip.html',
  styleUrl: './manage-trip.css',
})
export class ManageTrip implements OnInit {
  api        = inject(ApiService);
  auth       = inject(AuthService);
  route      = inject(ActivatedRoute);
  router     = inject(Router);
  cdr        = inject(ChangeDetectorRef);
  platformId = inject(PLATFORM_ID);

  trip: Trip | null   = null;
  driver: User | null = null;
  loading       = true;
  actionLoading: { [key: number]: string } = {}; // userId -> action type
  actionError   = '';
  actionSuccess = '';

  // Animation helpers
  acceptedIds: Set<number> = new Set();
  rejectedIds: Set<number> = new Set();
  removedIds: Set<number> = new Set();

  ngOnInit(): void {
    if (!isPlatformBrowser(this.platformId)) { this.loading = false; return; }

    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!id) { this.loading = false; return; }

    this.api.getTripById(id).subscribe({
      next: t => {
        this.trip    = t;
        this.loading = false;
        this.cdr.detectChanges();
        if (t.carDTO?.id) {
          this.loadDriver(t.carDTO.id);
        }
      },
      error: () => { this.loading = false; this.cdr.detectChanges(); },
    });
  }

  private loadDriver(carId: number) {
    this.api.getCarOwner(carId).subscribe({
      next: u => { this.driver = u; this.cdr.detectChanges(); },
      error: e => console.error('[ManageTrip] getCarOwner error:', e),
    });
  }

  get freeSeats(): number {
    return (this.trip?.carDTO?.capacity ?? 0) - (this.trip?.passengersDTO?.length ?? 0);
  }

  get requesters(): UserSummary[] {
    return this.trip?.requestersDTO ? [...this.trip.requestersDTO] : [];
  }

  get passengers(): UserSummary[] {
    return this.trip?.passengersDTO ? [...this.trip.passengersDTO] : [];
  }

  get totalSeats(): number {
    return this.trip?.carDTO?.capacity ?? 0;
  }

  get occupiedSeats(): number {
    return this.trip?.passengersDTO?.length ?? 0;
  }

  get isFull(): boolean {
    return this.freeSeats <= 0;
  }

  get occupancyPercent(): number {
    if (this.totalSeats === 0) return 0;
    return (this.occupiedSeats / this.totalSeats) * 100;
  }

  get currentUserId(): number | null { return this.auth.getUser()?.id ?? null; }

  get isDriver(): boolean {
    return this.driver?.id === this.currentUserId;
  }

  formatDate(d: string): string {
    if (!d) return '';
    return new Date(d).toLocaleDateString('es-ES', { weekday: 'long', day: 'numeric', month: 'long', year: 'numeric' });
  }

  isActionLoading(userId: number, action: string): boolean {
    return this.actionLoading[userId] === action;
  }

  acceptRequest(requester: UserSummary) {
    if (!this.trip || this.actionLoading[requester.id]) return;
    if (this.isFull) {
      this.actionError = 'No hay plazas libres disponibles.';
      this.clearMessages();
      return;
    }

    this.actionLoading[requester.id] = 'accept';
    this.actionError = '';
    this.actionSuccess = '';

    this.api.acceptPassenger(this.trip.id, requester.id).subscribe({
      next: t => {
        this.acceptedIds.add(requester.id);
        this.cdr.detectChanges();
        // Small delay for animation
        setTimeout(() => {
          this.trip = t;
          this.acceptedIds.delete(requester.id);
          delete this.actionLoading[requester.id];
          this.actionSuccess = `${requester.name} ha sido aceptado como pasajero.`;
          this.clearMessages();
          this.cdr.detectChanges();
        }, 400);
      },
      error: e => {
        this.actionError = e?.error?.message || 'Error al aceptar solicitud.';
        delete this.actionLoading[requester.id];
        this.clearMessages();
        this.cdr.detectChanges();
      },
    });
  }

  rejectRequest(requester: UserSummary) {
    if (!this.trip || this.actionLoading[requester.id]) return;

    this.actionLoading[requester.id] = 'reject';
    this.actionError = '';
    this.actionSuccess = '';

    this.api.rejectPassenger(this.trip.id, requester.id).subscribe({
      next: t => {
        this.rejectedIds.add(requester.id);
        this.cdr.detectChanges();
        setTimeout(() => {
          this.trip = t;
          this.rejectedIds.delete(requester.id);
          delete this.actionLoading[requester.id];
          this.actionSuccess = `Solicitud de ${requester.name} rechazada.`;
          this.clearMessages();
          this.cdr.detectChanges();
        }, 400);
      },
      error: e => {
        this.actionError = e?.error?.message || 'Error al rechazar solicitud.';
        delete this.actionLoading[requester.id];
        this.clearMessages();
        this.cdr.detectChanges();
      },
    });
  }

  removePassenger(passenger: UserSummary) {
    if (!this.trip || this.actionLoading[passenger.id]) return;

    this.actionLoading[passenger.id] = 'remove';
    this.actionError = '';
    this.actionSuccess = '';

    this.api.leaveTrip(this.trip.id, passenger.id).subscribe({
      next: t => {
        this.removedIds.add(passenger.id);
        this.cdr.detectChanges();
        setTimeout(() => {
          this.trip = t;
          this.removedIds.delete(passenger.id);
          delete this.actionLoading[passenger.id];
          this.actionSuccess = `${passenger.name} ha sido eliminado del viaje.`;
          this.clearMessages();
          this.cdr.detectChanges();
        }, 400);
      },
      error: e => {
        this.actionError = e?.error?.message || 'Error al eliminar pasajero.';
        delete this.actionLoading[passenger.id];
        this.clearMessages();
        this.cdr.detectChanges();
      },
    });
  }

  deleteTrip() {
    if (!this.trip) return;
    if (this.trip.passengersDTO.length > 0) {
      this.actionError = 'No puedes eliminar un viaje con pasajeros confirmados. Elimínalos primero.';
      this.clearMessages();
      return;
    }
    this.api.deleteTrip(this.trip.id).subscribe({
      next: () => {
        this.router.navigate(['/my-trips']);
      },
      error: e => {
        this.actionError = e?.error?.message || 'Error al eliminar el viaje.';
        this.clearMessages();
        this.cdr.detectChanges();
      },
    });
  }

  private clearMessages() {
    setTimeout(() => {
      this.actionError = '';
      this.actionSuccess = '';
      this.cdr.detectChanges();
    }, 4000);
  }
}
