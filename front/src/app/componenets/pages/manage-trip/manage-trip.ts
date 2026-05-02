import { Component, inject, OnInit, ChangeDetectorRef, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';
import { ApiService } from '../../../services/api-service';
import { AuthService } from '../../../services/auth-service';
import { Trip } from '../../../models/trip.model';
import { User, UserSummary, Rating } from '../../../models/user.model';

@Component({
    selector: 'page-manage-trip',
    standalone: true,
    imports: [CommonModule, RouterLink, Header, Footer],
    templateUrl: './manage-trip.html',
    styleUrl: './manage-trip.css',
})
export class ManageTrip implements OnInit {

    // ── Services injected ───────────────────────────────────────
    api = inject(ApiService);
    auth = inject(AuthService);
    route = inject(ActivatedRoute);
    router = inject(Router);
    cdr = inject(ChangeDetectorRef);
    platformId = inject(PLATFORM_ID);

    // ── Trip data ──────────────────────────────────────────────
    trip: Trip | null = null;
    driver: User | null = null;
    loading = true;

    // ── UI state (actions + feedback messages) ────────────────
    actionLoading: { [key: number]: string } = {}; // userId -> action type
    actionError = '';
    actionSuccess = '';

    // ── Animation helpers (track UI transitions) ───────────────
    acceptedIds: Set<number> = new Set();
    rejectedIds: Set<number> = new Set();
    removedIds: Set<number> = new Set();

    // ── Inline rating state per passenger ──────────────────────
    pRating: { [id: number]: number } = {};
    pLoading: { [id: number]: boolean } = {};
    pSaved: { [id: number]: boolean } = {};

    // ── Initialization ─────────────────────────────────────────
    ngOnInit(): void {
        if (!isPlatformBrowser(this.platformId)) {
            this.loading = false;
            return;
        }

        const id = Number(this.route.snapshot.paramMap.get('id'));
        if (!id) {
            this.loading = false;
            return;
        }

        // Load trip by id
        this.api.getTripById(id).subscribe({
            next: t => {
                this.trip = t;
                this.loading = false;
                this.cdr.detectChanges();

                // If trip has a car, load driver info
                if (t.carDTO?.id) {
                    this.loadDriver(t.carDTO.id);
                }
            },
            error: () => {
                this.loading = false;
                this.cdr.detectChanges();
            },
        });
    }

    // ── Driver loading ─────────────────────────────────────────
    private loadDriver(carId: number) {
        this.api.getCarOwner(carId).subscribe({
            next: u => {
                this.driver = u;
                this.initRatingState();
                this.cdr.detectChanges();
            },
            error: e => console.error('[ManageTrip] getCarOwner error:', e),
        });
    }

    // ── Initialize rating state for each passenger ─────────────
    private initRatingState() {
        if (this.trip?.passengersDTO) {
            for (const p of this.trip.passengersDTO) {
                const existing = this.existingRatingFor(p.id);
                this.pRating[p.id] = existing ? existing.rating : 0;
            }
        }
    }

    // ── Find existing rating from driver ───────────────────────
    existingRatingFor(passengerId: number): Rating | null {
        if (!this.driver?.ratingsDoneDTO) return null;
        return this.driver.ratingsDoneDTO.find(
            r => r.ratedUserDTO?.id === passengerId
        ) ?? null;
    }

    // ── Star rating click handler ──────────────────────────────
    clickStar(passengerId: number, star: number) {
        if (!this.currentUserId || this.pLoading[passengerId]) return;

        this.pRating[passengerId] = star;
        this.pLoading[passengerId] = true;
        this.pSaved[passengerId] = false;

        const existing = this.existingRatingFor(passengerId);

        // Update existing rating
        if (existing) {
            this.api.updateRating(existing.id, { rating: star }).subscribe({
                next: () => {
                    existing.rating = star;
                    this.flashSaved(passengerId);
                },
                error: () => {
                    this.pLoading[passengerId] = false;
                    this.cdr.detectChanges();
                },
            });
        }
        // Create new rating
        else {
            this.api.createRating({
                rating: star,
                idUserRate: this.currentUserId,
                idRatedUser: passengerId
            }).subscribe({
                next: (r) => {
                    if (this.driver!.ratingsDoneDTO) {
                        this.driver!.ratingsDoneDTO.push(r);
                    } else {
                        this.driver!.ratingsDoneDTO = [r];
                    }
                    this.flashSaved(passengerId);
                },
                error: () => {
                    this.pLoading[passengerId] = false;
                    this.cdr.detectChanges();
                },
            });
        }
    }

    // ── Show saved animation feedback ──────────────────────────
    private flashSaved(id: number) {
        this.pLoading[id] = false;
        this.pSaved[id] = true;
        this.cdr.detectChanges();

        setTimeout(() => {
            this.pSaved[id] = false;
            this.cdr.detectChanges();
        }, 1800);
    }

    // ── Trip calculations ──────────────────────────────────────
    get freeSeats(): number {
        return (this.trip?.carDTO?.capacity ?? 0) -
            (this.trip?.passengersDTO?.length ?? 0);
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

    // ── Auth / role helpers ────────────────────────────────────
    get currentUserId(): number | null {
        return this.auth.getUser()?.id ?? null;
    }

    get isDriver(): boolean {
        return this.driver?.id === this.currentUserId;
    }

    // ── Formatting helpers ─────────────────────────────────────
    formatDate(d: string): string {
        if (!d) return '';
        return new Date(d).toLocaleDateString('es-ES', {
            weekday: 'long',
            day: 'numeric',
            month: 'long',
            year: 'numeric'
        });
    }

    // ── Action state helpers ───────────────────────────────────
    isActionLoading(userId: number, action: string): boolean {
        return this.actionLoading[userId] === action;
    }

    // ── Accept passenger request ───────────────────────────────
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

    // ── Reject request ─────────────────────────────────────────
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

    // ── Remove passenger ───────────────────────────────────────
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

    // ── Delete trip ────────────────────────────────────────────
    deleteTrip() {
        if (!this.trip) return;

        if (this.trip.passengersDTO.length > 0) {
            this.actionError = 'No puedes eliminar un viaje con pasajeros confirmados. Elimínalos primero.';
            this.clearMessages();
            return;
        }

        this.api.deleteTrip(this.trip.id).subscribe({
            next: () => this.router.navigate(['/my-trips']),
            error: e => {
                this.actionError = e?.error?.message || 'Error al eliminar el viaje.';
                this.clearMessages();
                this.cdr.detectChanges();
            },
        });
    }

    // ── Auto-clear messages ────────────────────────────────────
    private clearMessages() {
        setTimeout(() => {
            this.actionError = '';
            this.actionSuccess = '';
            this.cdr.detectChanges();
        }, 4000);
    }
}