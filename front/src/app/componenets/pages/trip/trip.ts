import { Component, inject, OnInit, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';
import { AppIcon } from '../../elements/icon/icon';
import { ApiService } from '../../../services/api-service';
import { AuthService } from '../../../services/auth-service';
import { Campus } from '../../../models/campus.model';
import { Town } from '../../../models/town.model';
import { Car } from '../../../models/car.model';

@Component({
  selector: 'pages-trip',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink, Header, Footer, AppIcon],
  templateUrl: './trip.html',
  styleUrl: './trip.css',
})
export class PageTrip implements OnInit {
  fb           = inject(FormBuilder);
  api          = inject(ApiService);
  auth         = inject(AuthService);
  router       = inject(Router);
  platformId   = inject(PLATFORM_ID);

  campuses: Campus[] = [];
  towns: Town[]      = [];
  cars: Car[]        = [];
  loading            = false;
  success            = false;
  error              = '';

  form = this.fb.group({
    idCar:            [null as number | null, Validators.required],
    idCampus:         [null as number | null, Validators.required],
    idTown:           [null as number | null, Validators.required],
    isToCampus:       [true, Validators.required],
    departureDate:    ['', Validators.required],
    departureTime:    ['', Validators.required],
    departureAddress: ['', Validators.required],
    price:            [null as number | null, [Validators.required, Validators.min(0)]],
  });

  ngOnInit() {
    if (!isPlatformBrowser(this.platformId)) return;
    this.api.getCampuses().subscribe({ next: c => this.campuses = c, error: () => {} });
    this.api.getTowns().subscribe({   next: t => this.towns = t,    error: () => {} });
    const userId = this.auth.getUser()?.id;
    if (userId) {
      this.api.getUser(userId).subscribe({
        next: u => this.cars = u.carsDTO || [],
        error: () => {},
      });
    }
  }

  submit() {
    if (this.form.invalid || this.loading) return;
    this.loading = true; this.error = '';
    this.api.createTrip(this.form.value).subscribe({
      next: () => { this.loading = false; this.success = true; setTimeout(() => this.router.navigate(['/profile']), 2000); },
      error: e  => { this.error = e?.error?.message || 'Error al publicar el viaje.'; this.loading = false; },
    });
  }
}
