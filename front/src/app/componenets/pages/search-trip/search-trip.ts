import { Component, inject, OnInit, OnDestroy } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Subject, switchMap, tap, catchError, of } from 'rxjs';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';
import { TripCard } from '../../elements/trip/trip';
import { ApiService } from '../../../services/api-service';
import { Trip } from '../../../models/trip.model';
import { Campus } from '../../../models/campus.model';
import { Town } from '../../../models/town.model';

@Component({
  selector: 'page-search-trip',
  standalone: true,
  imports: [FormsModule, CommonModule, Header, Footer, TripCard],
  templateUrl: './search-trip.html',
  styleUrl: './search-trip.css',
})
export class SearchTrip implements OnInit, OnDestroy {
  api = inject(ApiService);

  trips: Trip[]      = [];
  campuses: Campus[] = [];
  towns: Town[]      = [];
  loading            = false;
  totalPages         = 0;
  currentPage        = 0;

  filters = {
    campusId:      null as number | null,
    townId:        null as number | null,
    isToCampus:    null as boolean | null,
    departureDate: '',
    maxPrice:      null as number | null,
    minFreeSeats:  null as number | null,
  };

  private search$ = new Subject<{ filters: any; page: number }>();

  ngOnInit() {
    this.api.getCampuses().subscribe({ next: c => this.campuses = c, error: () => {} });
    this.api.getTowns().subscribe({    next: t => this.towns    = t, error: () => {} });

    this.search$.pipe(
      tap(() => { this.loading = true; }),
      switchMap(({ filters, page }) =>
        this.api.searchTrips(filters, page).pipe(
          catchError(() => of(null))
        )
      )
    ).subscribe(p => {
      this.loading = false;
      if (p) {
        this.trips      = p.content;
        this.totalPages = p.totalPages;
      }
    });

    this.search();
  }

  ngOnDestroy() {
    this.search$.complete();
  }

  // Cambia la dirección Y busca inmediatamente
  setDirection(value: boolean | null) {
    this.filters.isToCampus = value;
    this.search();
  }

  search(page = 0) {
    this.currentPage = page;

    const f: any = {};
    if (this.filters.campusId)            f.campusId      = +this.filters.campusId;
    if (this.filters.townId)              f.townId        = +this.filters.townId;
    if (this.filters.isToCampus !== null) f.isToCampus    =  this.filters.isToCampus;
    if (this.filters.departureDate)       f.departureDate =  this.filters.departureDate;
    if (this.filters.maxPrice)            f.maxPrice      = +this.filters.maxPrice;
    if (this.filters.minFreeSeats)        f.minFreeSeats  = +this.filters.minFreeSeats;

    this.search$.next({ filters: f, page });
  }

  reset() {
    this.filters = { campusId: null, townId: null, isToCampus: null,
                     departureDate: '', maxPrice: null, minFreeSeats: null };
    this.search();
  }

  updateTrip(updated: Trip, i: number) { this.trips[i] = updated; }
  pages(): number[] { return Array.from({ length: this.totalPages }, (_, i) => i); }
}
