import { Component, inject, OnInit, ChangeDetectorRef, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { FormBuilder, Validators, ReactiveFormsModule, AbstractControl } from '@angular/forms';
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
import { DayOfWeek, WEEK_DAYS } from '../../../models/periodic-trip.model';
import { finalize } from 'rxjs';

// Función de validación personalizada para verificar que la fecha de salida no sea anterior a la fecha actual
function verifyDate(control: AbstractControl) {
    if (!control.value) return null;
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const inputDate = new Date(control.value);
    inputDate.setHours(0, 0, 0, 0);
    return inputDate >= today ? null : { pastDate: true };
}
// Función de validación personalizada para verificar que la hora de salida no sea anterior a la hora actual si la fecha de salida es hoy
function verifyTime(control: AbstractControl) {
    if (!control.parent) return null;

    const departureDate = control.parent.get('departureDate')?.value;
    const departureTime = control.value;

    if (!departureDate || !departureTime) return null;

    const today = new Date();
    const selectedDate = new Date(departureDate);

    // Comparar solo fecha
    today.setHours(0, 0, 0, 0);
    selectedDate.setHours(0, 0, 0, 0);

    // Si no es hoy, no validar hora
    if (selectedDate.getTime() !== today.getTime()) {
        return null;
    }

    // Hora actual
    const now = new Date();

    const [hours, minutes] = departureTime.split(':').map(Number);

    const selectedTime = new Date();
    selectedTime.setHours(hours, minutes, 0, 0);

    return selectedTime >= now ? null : { pastTime: true };
}

type TripMode = 'single' | 'periodic';

@Component({
    selector: 'pages-trip',
    standalone: true,
    imports: [ReactiveFormsModule, CommonModule, RouterLink, Header, Footer, AppIcon],
    templateUrl: './trip.html',
    styleUrl: './trip.css',
})
export class PageTrip implements OnInit {

    // Inyección de servicios
    fb = inject(FormBuilder);
    api = inject(ApiService);
    auth = inject(AuthService);
    router = inject(Router);
    cdr = inject(ChangeDetectorRef);
    platformId = inject(PLATFORM_ID);

    // Datos de apoyo
    campuses: Campus[] = [];
    towns: Town[] = [];
    cars: Car[] = [];
    carsLoading = false;

    // Estado del formulario
    loading = false;
    success = false;
    error = '';
    submited = false;

    // Modo: viaje puntual o viaje periódico
    mode: TripMode = 'single';

    // Nº de viajes generados tras crear una serie periódica (para el mensaje de éxito)
    generatedCount = 0;

    // Días de la semana disponibles y su selección (solo modo periódico)
    readonly weekDays = WEEK_DAYS;
    selectedDays: Record<DayOfWeek, boolean> = {
        MONDAY: false, TUESDAY: false, WEDNESDAY: false, THURSDAY: false,
        FRIDAY: false, SATURDAY: false, SUNDAY: false,
    };
    daysError = false;
    dateRangeError = '';

    // Formulario reactivo. Los controles específicos de cada modo se
    // habilitan/deshabilitan en setMode(); los deshabilitados no cuentan
    // para la validez ni se envían.
    form = this.fb.group({
        idCar: [null as number | null, Validators.required],
        idCampus: [null, Validators.required],
        idTown: [null, Validators.required],
        isToCampus: [true, Validators.required],
        departureAddress: [null, Validators.required],
        departureTime: [null, [Validators.required, verifyTime]],
        price: [null as number | null, [Validators.required, Validators.min(0.5)]],
        // Solo modo puntual
        departureDate: [null as string | null, [Validators.required, verifyDate]],
        // Solo modo periódico
        startDate: [{ value: null as string | null, disabled: true }, [Validators.required, verifyDate]],
        endDate: [{ value: null as string | null, disabled: true }, Validators.required],
        repeatIntervalWeeks: [{ value: 1 as number | null, disabled: true }, [Validators.required, Validators.min(1)]],
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

    // Cambia de modo puntual/periódico y ajusta qué controles están activos
    setMode(mode: TripMode) {
        if (this.mode === mode) return;
        this.mode = mode;
        this.error = '';
        this.daysError = false;
        this.dateRangeError = '';

        if (mode === 'periodic') {
            this.form.get('departureDate')?.disable();
            this.form.get('startDate')?.enable();
            this.form.get('endDate')?.enable();
            this.form.get('repeatIntervalWeeks')?.enable();
        } else {
            this.form.get('departureDate')?.enable();
            this.form.get('startDate')?.disable();
            this.form.get('endDate')?.disable();
            this.form.get('repeatIntervalWeeks')?.disable();
        }
        this.cdr.detectChanges();
    }

    toggleDay(day: DayOfWeek) {
        this.selectedDays[day] = !this.selectedDays[day];
        if (this.getSelectedDays().length > 0) this.daysError = false;
    }

    getSelectedDays(): DayOfWeek[] {
        return this.weekDays.map(d => d.value).filter(d => this.selectedDays[d]);
    }

    // Envío del formulario según el modo activo
    submit() {
        this.submited = true;
        this.error = '';

        if (this.mode === 'periodic') {
            this.submitPeriodic();
            return;
        }

        if (this.form.invalid || this.loading) return;

        this.loading = true;
        const v = this.form.getRawValue();
        const dto = {
            idCar: Number(v.idCar),
            idCampus: v.idCampus ? Number(v.idCampus) : null,
            idTown: v.idTown ? Number(v.idTown) : null,
            isToCampus: v.isToCampus,
            departureDate: v.departureDate,
            departureTime: v.departureTime,
            departureAddress: v.departureAddress,
            price: v.price,
        };
        setTimeout(() => {
            this.api.createTrip(dto).pipe(finalize(() => {
                this.loading = false;
                this.cdr.detectChanges();
            })).subscribe({
                next: () => {
                    this.success = true;
                    setTimeout(() => this.router.navigate(['/my-trips']), 2000);
                },
                error: e => {
                    this.error = e?.error?.message || 'Error al publicar el viaje.';
                    this.loading = false;
                    this.cdr.detectChanges();
                },
            });
        }, 500);
    }

    private submitPeriodic() {
        const days = this.getSelectedDays();
        this.daysError = days.length === 0;
        this.dateRangeError = '';

        const v = this.form.getRawValue();
        if (v.startDate && v.endDate) {
            const start = new Date(v.startDate);
            const end = new Date(v.endDate);
            if (end < start) {
                this.dateRangeError = 'La fecha de fin no puede ser anterior a la de inicio.';
            } else {
                const maxEnd = new Date(start);
                maxEnd.setFullYear(maxEnd.getFullYear() + 1);
                if (end > maxEnd) this.dateRangeError = 'El rango de fechas no puede superar un año.';
            }
        }

        if (this.form.invalid || this.daysError || this.dateRangeError || this.loading) return;

        this.loading = true;
        const dto = {
            idCar: Number(v.idCar),
            idCampus: Number(v.idCampus),
            idTown: Number(v.idTown),
            isToCampus: !!v.isToCampus,
            departureAddress: v.departureAddress!,
            price: Number(v.price),
            startDate: v.startDate!,
            endDate: v.endDate!,
            departureTime: v.departureTime!,
            daysOfWeek: days,
            repeatIntervalWeeks: Number(v.repeatIntervalWeeks) || 1,
        };
        setTimeout(() => {
            this.api.createPeriodicTrip(dto).pipe(finalize(() => {
                this.loading = false;
                this.cdr.detectChanges();
            })).subscribe({
                next: (res) => {
                    this.generatedCount = res?.tripsGenerated ?? 0;
                    this.success = true;
                    this.cdr.detectChanges();
                    setTimeout(() => this.router.navigate(['/my-trips'], { queryParams: { tab: 'periodic' } }), 2500);
                },
                error: e => {
                    this.error = e?.error?.message || 'Error al crear el viaje periódico.';
                    this.loading = false;
                    this.cdr.detectChanges();
                },
            });
        }, 500);
    }
}
