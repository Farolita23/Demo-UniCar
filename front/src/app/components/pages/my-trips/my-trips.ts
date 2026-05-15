import { Component, inject, OnInit, ChangeDetectorRef, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';
import { ApiService } from '../../../services/api-service';
import { AuthService } from '../../../services/auth-service';
import { Trip } from '../../../models/trip.model';
import { TripCard } from '../../elements/trip/trip';

@Component({
    selector: 'page-my-trips',
    standalone: true,
    imports: [CommonModule, RouterLink, Header, Footer, TripCard],
    templateUrl: './my-trips.html',
    styleUrl: './my-trips.css',
})
// Página de "Mis Viajes" que muestra los viajes del usuario como conductor y pasajero
export class MyTrips implements OnInit {

    // Inyección de servicios
    api = inject(ApiService);
    auth = inject(AuthService);
    router = inject(Router);
    cdr = inject(ChangeDetectorRef);
    platformId = inject(PLATFORM_ID);

    // Listas de viajes y estados de carga
    tripsAsPassenger: Trip[] = [];
    tripsAsDriver: Trip[] = [];
    loadingPassenger = true;
    loadingDriver = true;

    // Control de pestañas
    activeTab: 'passenger' | 'driver' = 'passenger';

    // Métodos para navegación y lógica de viajes
    ngOnInit(): void {
        if (!isPlatformBrowser(this.platformId)) {
            this.loadingPassenger = false;
            this.loadingDriver = false;
            return;
        }
        const userId = this.auth.getUser()?.id;
        if (!userId) { this.router.navigate(['/login']); return; }

        this.api.getTripsAsPassenger(userId).subscribe({
            next: p => { this.tripsAsPassenger = p.content; this.loadingPassenger = false; this.cdr.detectChanges(); },
            error: () => { this.loadingPassenger = false; this.cdr.detectChanges(); },
        });
        this.api.getTripsAsDriver(userId).subscribe({
            next: p => { this.tripsAsDriver = p.content; this.loadingDriver = false; this.cdr.detectChanges(); },
            error: () => { this.loadingDriver = false; this.cdr.detectChanges(); },
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
}
