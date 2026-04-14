import { Component, inject, OnInit, ChangeDetectorRef, PLATFORM_ID } from '@angular/core';
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
  fb         = inject(FormBuilder);
  api        = inject(ApiService);
  auth       = inject(AuthService);
  router     = inject(Router);
  cdr        = inject(ChangeDetectorRef);
  platformId = inject(PLATFORM_ID);

  campuses: Campus[] = [];
  towns: Town[]      = [];
  cars: Car[]        = [];
  carsLoading        = false;
  loading            = false;
  success            = false;
  error              = '';

  form = this.fb.group({
    idCar:            [null as number | null, Validators.required],
    idCampus:         ['', Validators.required],
    idTown:           ['', Validators.required],
    isToCampus:       [true, Validators.required],
    departureDate:    ['', Validators.required],
    departureTime:    ['', Validators.required],
    departureAddress: ['', Validators.required],
    price:            [null as number | null, [Validators.required, Validators.min(0)]],
  });

  ngOnInit(): void {
    if (!isPlatformBrowser(this.platformId)) return;

    this.api.getCampuses().subscribe({
      next: c => { this.campuses = c; this.cdr.detectChanges(); },
      error: e => console.error('[PageTrip] getCampuses error:', e),
    });
    this.api.getTowns().subscribe({
      next: t => { this.towns = t; this.cdr.detectChanges(); },
      error: e => console.error('[PageTrip] getTowns error:', e),
    });

    const userId = this.auth.getUser()?.id;
    if (userId) {
      this.carsLoading = true;
      this.api.getCarsByUser(userId).subscribe({
        next: cars => {
          this.cars = cars;
          this.carsLoading = false;
          this.cdr.detectChanges();
        },
        error: e => {
          console.error('[PageTrip] getCarsByUser error:', e?.status, e?.error);
          this.carsLoading = false;
          this.cdr.detectChanges();
        },
      });
    }
  }

  submit() {
    if (this.form.invalid || this.loading) return;
    this.loading = true;
    this.error   = '';
    const v = this.form.value;
    const dto = {
      idCar:            Number(v.idCar),
      idCampus:         v.idCampus  ? Number(v.idCampus)  : null,
      idTown:           v.idTown    ? Number(v.idTown)    : null,
      isToCampus:       v.isToCampus,
      departureDate:    v.departureDate,
      departureTime:    v.departureTime,
      departureAddress: v.departureAddress,
      price:            v.price,
    };
    this.api.createTrip(dto).subscribe({
      next: () => {
        this.loading = false;
        this.success = true;
        this.cdr.detectChanges();
        setTimeout(() => this.router.navigate(['/profile']), 2000);
      },
      error: e => {
        this.error   = e?.error?.message || 'Error al publicar el viaje.';
        this.loading = false;
        this.cdr.detectChanges();
      },
    });
  }
}
