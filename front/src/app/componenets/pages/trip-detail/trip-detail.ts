import { Component, inject, OnInit, ChangeDetectorRef, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';
import { ApiService } from '../../../services/api-service';
import { AuthService } from '../../../services/auth-service';
import { Trip } from '../../../models/trip.model';
import { User, Rating } from '../../../models/user.model';

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

  // ── Inline rating state ───────────────────────────────────────
  /** Current star value shown for the driver (set from existing or hover) */
  driverRatingValue = 0;
  driverRatingLoading = false;
  driverRatingSaved = false;

  /** Per-passenger: current value, loading, saved */
  pRating:   { [id: number]: number  } = {};
  pLoading:  { [id: number]: boolean } = {};
  pSaved:    { [id: number]: boolean } = {};
  pError:    { [id: number]: string  } = {};

  ngOnInit(): void {
    if (!isPlatformBrowser(this.platformId)) { this.loading = false; return; }

    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!id) { this.loading = false; return; }

    this.api.getTripById(id).subscribe({
      next: t => {
        this.trip    = t;
        this.loading = false;
        this.cdr.detectChanges();
        if (t.carDTO?.id) { this.loadDriver(t.carDTO.id); }
      },
      error: () => { this.loading = false; this.cdr.detectChanges(); },
    });
  }

  private loadDriver(carId: number) {
    this.api.getCarOwner(carId).subscribe({
      next: u => {
        this.driver = u;
        this.initRatingState();
        this.cdr.detectChanges();
      },
      error: e => console.error('[TripDetail] getCarOwner error:', e),
    });
  }

  private initRatingState() {
    // Pre-fill driver rating if current user already rated
    const existing = this.existingDriverRating();
    if (existing) { this.driverRatingValue = existing.rating; }

    // Pre-fill passenger ratings if driver already rated them
    if (this.isDriver && this.trip?.passengersDTO) {
      for (const p of this.trip.passengersDTO) {
        const r = this.existingPassengerRating(p.id);
        this.pRating[p.id] = r ? r.rating : 0;
      }
    }
  }

  // ── Getters ───────────────────────────────────────────────────
  get freeSeats(): number {
    return (this.trip?.carDTO?.capacity ?? 0) - (this.trip?.passengersDTO?.length ?? 0);
  }
  get isFull(): boolean { return this.freeSeats <= 0; }
  get currentUserId(): number | null { return this.auth.getUser()?.id ?? null; }
  get isDriver(): boolean { return !!this.driver && this.driver.id === this.currentUserId; }
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

  // ── Rating helpers ────────────────────────────────────────────
  existingDriverRating(): Rating | null {
    if (!this.driver?.ratingsReceivedDTO || !this.currentUserId) return null;
    return this.driver.ratingsReceivedDTO.find(r => r.userRateDTO?.id === this.currentUserId) ?? null;
  }

  existingPassengerRating(passengerId: number): Rating | null {
    if (!this.driver?.ratingsDoneDTO) return null;
    return this.driver.ratingsDoneDTO.find(r => r.ratedUserDTO?.id === passengerId) ?? null;
  }

  // ── Auto-save: click a star → instant save ────────────────────

  clickDriverStar(star: number) {
    if (!this.currentUserId || !this.driver || this.driverRatingLoading) return;
    this.driverRatingValue = star;
    this.driverRatingLoading = true;
    this.driverRatingSaved = false;

    const existing = this.existingDriverRating();
    if (existing) {
      this.api.updateRating(existing.id, { rating: star }).subscribe({
        next: () => {
          existing.rating = star;
          this.flashSaved('driver');
        },
        error: () => { this.driverRatingLoading = false; this.cdr.detectChanges(); },
      });
    } else {
      this.api.createRating({
        rating: star, idUserRate: this.currentUserId, idRatedUser: this.driver.id
      }).subscribe({
        next: (r) => {
          if (this.driver!.ratingsReceivedDTO) { this.driver!.ratingsReceivedDTO.push(r); }
          else { this.driver!.ratingsReceivedDTO = [r]; }
          this.flashSaved('driver');
        },
        error: () => { this.driverRatingLoading = false; this.cdr.detectChanges(); },
      });
    }
  }

  clickPassengerStar(passengerId: number, star: number) {
    if (!this.currentUserId || this.pLoading[passengerId]) return;
    this.pRating[passengerId] = star;
    this.pLoading[passengerId] = true;
    this.pSaved[passengerId] = false;
    this.pError[passengerId] = '';

    const existing = this.existingPassengerRating(passengerId);
    if (existing) {
      this.api.updateRating(existing.id, { rating: star }).subscribe({
        next: () => {
          existing.rating = star;
          this.flashSaved('passenger', passengerId);
        },
        error: e => {
          this.pError[passengerId] = e?.error?.message || 'Error';
          this.pLoading[passengerId] = false;
          this.cdr.detectChanges();
        },
      });
    } else {
      this.api.createRating({
        rating: star, idUserRate: this.currentUserId, idRatedUser: passengerId
      }).subscribe({
        next: (r) => {
          if (this.driver!.ratingsDoneDTO) { this.driver!.ratingsDoneDTO.push(r); }
          else { this.driver!.ratingsDoneDTO = [r]; }
          this.flashSaved('passenger', passengerId);
        },
        error: e => {
          this.pError[passengerId] = e?.error?.message || 'Error';
          this.pLoading[passengerId] = false;
          this.cdr.detectChanges();
        },
      });
    }
  }

  /** Brief "saved" flash feedback */
  private flashSaved(target: 'driver' | 'passenger', passengerId?: number) {
    if (target === 'driver') {
      this.driverRatingLoading = false;
      this.driverRatingSaved = true;
      this.cdr.detectChanges();
      setTimeout(() => { this.driverRatingSaved = false; this.cdr.detectChanges(); }, 1800);
    } else {
      this.pLoading[passengerId!] = false;
      this.pSaved[passengerId!] = true;
      this.cdr.detectChanges();
      setTimeout(() => { this.pSaved[passengerId!] = false; this.cdr.detectChanges(); }, 1800);
    }
  }

  // ── Trip actions ──────────────────────────────────────────────
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
