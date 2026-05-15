import { Component, inject, OnInit, OnDestroy, ChangeDetectorRef, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Subject, switchMap, tap, catchError, of, filter } from 'rxjs';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';
import { TripCard } from '../../elements/trip/trip';
import { AppIcon } from '../../elements/icon/icon';
import { ApiService } from '../../../services/api-service';
import { Trip } from '../../../models/trip.model';
import { Campus } from '../../../models/campus.model';
import { Town } from '../../../models/town.model';

@Component({
    selector: 'page-search-trip',
    standalone: true,
    imports: [FormsModule, CommonModule, Header, Footer, TripCard, AppIcon],
    templateUrl: './search-trip.html',
    styleUrl: './search-trip.css',
})
export class SearchTrip implements OnInit, OnDestroy {

    // Inyección de servicios
    api = inject(ApiService);
    cdr = inject(ChangeDetectorRef);
    platformId = inject(PLATFORM_ID);

    // Variables para almacenar los viajes, campus y pueblos obtenidos de la API
    trips: Trip[] = [];
    campuses: Campus[] = [];
    towns: Town[] = [];
    
    // Variable para indicar si se está cargando la búsqueda de viajes
    loading = false;

    // Paginación
    totalPages = 0;
    totalTrips = 0;
    currentPage = 0;

    // Filtros de búsqueda
    filters = {
        campus: null as string | null,
        campusId: null as number | null,
        town: null as string | null,
        townId: null as number | null,
        isToCampus: null as boolean | null,
        departureDate: '',
        maxPrice: 100 as number | null,
        minFreeSeats: null as number | null,
    };

    // Subject para manejar las búsquedas de viajes
    private search$ = new Subject<{ filters: any; page: number }>();

    // Método para inicializar el componente y cargar los datos necesarios
    ngOnInit(): void {
        if (!isPlatformBrowser(this.platformId)) return;

        this.search$.pipe(
            tap(() => { this.loading = true; this.cdr.detectChanges(); }),
            switchMap(({ filters, page }) =>
                this.api.searchTrips(filters, page).pipe(
                    catchError(err => { console.error('[SearchTrip] error:', err); return of(null); })
                )
            )
        ).subscribe(p => {
            this.loading = false;
            if (p) {
                const raw = p as any;
                this.trips = raw.content ?? [];
                this.totalPages = raw.totalPages ?? 0;
                this.totalTrips = raw.totalElements ?? 0;
            } else {
                this.trips = [];
            }
            this.cdr.detectChanges();
        });

        this.api.getCampuses().subscribe({
            next: c => { this.campuses = c; this.cdr.detectChanges(); },
            error: e => console.error('[SearchTrip] getCampuses error:', e),
        });
        this.api.getTowns().subscribe({
            next: t => { this.towns = t; this.cdr.detectChanges(); },
            error: e => console.error('[SearchTrip] getTowns error:', e),
        });

        this.search();
    }

    // Método para limpiar recursos al destruir el componente
    ngOnDestroy() { 
        this.search$.complete();
    }

    // Método para establecer la dirección del viaje (hacia o desde el campus, o ambos)
    setDirection(value: boolean | null) { 
        this.filters.isToCampus = value; 
    }

    // Método para realizar la búsqueda de viajes según los filtros actuales y la página especificada
    search(page = 0) {
        this.currentPage = page;
        const f: any = {};
        if (this.filters.campus) f.campusId = +this.campuses.find(c => c.name === this.filters.campus)?.id!;
        if (this.filters.town) f.townId = +this.towns.find(t => t.name === this.filters.town)?.id!;
        if (this.filters.isToCampus !== null) f.isToCampus = this.filters.isToCampus;
        if (this.filters.departureDate) f.departureDate = this.filters.departureDate;
        if (this.filters.maxPrice) f.maxPrice = +this.filters.maxPrice;
        if (this.filters.minFreeSeats) f.minFreeSeats = +this.filters.minFreeSeats;
        this.search$.next({ filters: f, page });
    }

    // Método para resetear los filtros a sus valores iniciales y realizar una nueva búsqueda
    reset() {
        this.filters = {
            campus: null, town: null,
            campusId: null, townId: null, isToCampus: null,
            departureDate: '', maxPrice: 100, minFreeSeats: null
        };
        this.search();
    }

    // Método para actualizar un viaje específico en el array de viajes
    updateTrip(updated: Trip, i: number) {
        this.trips[i] = updated;
        this.cdr.detectChanges();
    }

    // Método para generar un array de números basado en el total de páginas
    pages(): number[] {
        return Array.from({ length: this.totalPages }, (_, i) => i);
    }
}
