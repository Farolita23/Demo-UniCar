import { Component, inject, OnInit, ChangeDetectorRef, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';
import { AppIcon } from '../../elements/icon/icon';
import { ApiService } from '../../../services/api-service';
import { AuthService } from '../../../services/auth-service';
import { Trip } from '../../../models/trip.model';
import { PeriodicTrip, DayOfWeek, WEEK_DAYS } from '../../../models/periodic-trip.model';
import { TripCard } from '../../elements/trip/trip';

@Component({
    selector: 'page-my-trips',
    standalone: true,
    imports: [CommonModule, RouterLink, Header, Footer, TripCard, AppIcon],
    templateUrl: './my-trips.html',
    styleUrl: './my-trips.css',
})
// Página de "Mis Viajes" que muestra los viajes del usuario como conductor, pasajero y sus series periódicas
export class MyTrips implements OnInit {

    // Inyección de servicios
    api = inject(ApiService);
    auth = inject(AuthService);
    router = inject(Router);
    route = inject(ActivatedRoute);
    cdr = inject(ChangeDetectorRef);
    platformId = inject(PLATFORM_ID);

    // Listas de viajes y estados de carga
    tripsAsPassenger: Trip[] = [];
    tripsAsDriver: Trip[] = [];
    periodicTrips: PeriodicTrip[] = [];
    loadingPassenger = true;
    loadingDriver = true;
    loadingPeriodic = true;

    // Control de pestañas
    activeTab: 'passenger' | 'driver' | 'periodic' = 'passenger';

    // Estado de expansión de series periódicas (ver viajes generados)
    expandedPeriodic: number | null = null;
    generatedTrips: Record<number, Trip[]> = {};
    loadingGenerated: Record<number, boolean> = {};
    deletingPeriodic: Record<number, boolean> = {};

    readonly weekDays = WEEK_DAYS;

    ngOnInit(): void {
        if (!isPlatformBrowser(this.platformId)) {
            this.loadingPassenger = false;
            this.loadingDriver = false;
            this.loadingPeriodic = false;
            return;
        }
        const userId = this.auth.getUser()?.id;
        if (!userId) { this.router.navigate(['/login']); return; }

        const tab = this.route.snapshot.queryParamMap.get('tab');
        if (tab === 'driver' || tab === 'passenger' || tab === 'periodic') {
            this.activeTab = tab;
        }

        this.api.getTripsAsPassenger(userId).subscribe({
            next: p => { this.tripsAsPassenger = p.content; this.loadingPassenger = false; this.cdr.detectChanges(); },
            error: () => { this.loadingPassenger = false; this.cdr.detectChanges(); },
        });
        this.api.getTripsAsDriver(userId).subscribe({
            next: p => { this.tripsAsDriver = p.content; this.loadingDriver = false; this.cdr.detectChanges(); },
            error: () => { this.loadingDriver = false; this.cdr.detectChanges(); },
        });
        this.api.getPeriodicTripsAsDriver(userId).subscribe({
            next: p => { this.periodicTrips = p.content; this.loadingPeriodic = false; this.cdr.detectChanges(); },
            error: () => { this.loadingPeriodic = false; this.cdr.detectChanges(); },
        });
    }

    // Navegar a detalle del viaje
    goToDetail(id: number) {
        this.router.navigate(['/trip-detail', id]);
    }

    // Navegar a gestión del viaje (solo conductor)
    goToManage(id: number) {
        this.router.navigate(['/manage-trip', id]);
    }

    // Navegar al perfil del conductor desde un viaje
    goToDriverProfile(trip: Trip, event: Event) {
        event.stopPropagation();
        if (trip.driverDTO?.id) this.router.navigate(['/user', trip.driverDTO.id]);
    }

    // Calcular plazas libres de un viaje
    freeSeats(trip: Trip): number {
        return (trip.carDTO?.capacity ?? 0) - (trip.passengersDTO?.length ?? 0);
    }

    // Verificar si un viaje está completo
    isFull(trip: Trip): boolean {
        return this.freeSeats(trip) <= 0;
    }

    // Formatear fecha de viaje para mostrar
    formatDate(d: string): string {
        if (!d) return '';
        return new Date(d).toLocaleDateString('es-ES', { weekday: 'short', day: 'numeric', month: 'short', year: 'numeric' });
    }

    // Formatear fecha corta (sin día de la semana) para rangos
    formatShort(d: string): string {
        if (!d) return '';
        return new Date(d).toLocaleDateString('es-ES', { day: 'numeric', month: 'short', year: 'numeric' });
    }

    // ── Series periódicas ──────────────────────────────────────

    // Etiqueta corta de los días de una serie: "L · X · V"
    dayLabels(pt: PeriodicTrip): string {
        const set = new Set(pt.daysOfWeek);
        return this.weekDays.filter(d => set.has(d.value)).map(d => d.short).join(' · ');
    }

    intervalLabel(pt: PeriodicTrip): string {
        return pt.repeatIntervalWeeks === 1
            ? 'todas las semanas'
            : `cada ${pt.repeatIntervalWeeks} semanas`;
    }

    // Mostrar/ocultar los viajes generados de una serie (carga perezosa)
    toggleGenerated(pt: PeriodicTrip) {
        if (this.expandedPeriodic === pt.id) {
            this.expandedPeriodic = null;
            return;
        }
        this.expandedPeriodic = pt.id;
        if (!this.generatedTrips[pt.id]) {
            this.loadingGenerated[pt.id] = true;
            this.api.getPeriodicTripGeneratedTrips(pt.id).subscribe({
                next: page => {
                    this.generatedTrips[pt.id] = page.content;
                    this.loadingGenerated[pt.id] = false;
                    this.cdr.detectChanges();
                },
                error: () => {
                    this.loadingGenerated[pt.id] = false;
                    this.cdr.detectChanges();
                },
            });
        }
    }

    // Eliminar una serie periódica (los viajes ya generados se conservan como puntuales)
    deletePeriodic(pt: PeriodicTrip) {
        if (this.deletingPeriodic[pt.id]) return;
        const ok = isPlatformBrowser(this.platformId)
            ? window.confirm('¿Eliminar esta serie periódica? Los viajes ya generados se mantienen, pero dejarán de estar agrupados.')
            : true;
        if (!ok) return;

        this.deletingPeriodic[pt.id] = true;
        this.api.deletePeriodicTrip(pt.id).subscribe({
            next: () => {
                this.periodicTrips = this.periodicTrips.filter(x => x.id !== pt.id);
                if (this.expandedPeriodic === pt.id) this.expandedPeriodic = null;
                delete this.deletingPeriodic[pt.id];
                this.cdr.detectChanges();
            },
            error: () => {
                delete this.deletingPeriodic[pt.id];
                this.cdr.detectChanges();
            },
        });
    }
}
