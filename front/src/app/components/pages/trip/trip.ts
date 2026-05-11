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
import { finalize } from 'rxjs';

// Función de validación personalizada para verificar que la fecha de salida no sea anterior a la fecha actual
function verifyDate(control: AbstractControl) {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const inputDate = new Date(control.value);
    inputDate.setHours(0, 0, 0, 0);
    console.log(today, "\n", inputDate);
    
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

    // Variables para almacenar los campus, pueblos y coches obtenidos de la API
    campuses: Campus[] = [];
    towns: Town[] = [];
    cars: Car[] = [];

    // Variable para indicar si se están cargando los coches del usuario
    carsLoading = false;

    // Variable para indicar si se está procesando el formulario de publicación de viajes
    loading = false;

    // Variable para mostrar mensajes de éxito o error en el formulario de publicación de viajes
    success = false;
    error = '';

    // Formulario reactivo para la publicación de viajes
    form = this.fb.group({
        idCar: [null as number | null, Validators.required],
        idCampus: [null, Validators.required],
        idTown: [null, Validators.required],
        isToCampus: [true, Validators.required],
        departureDate: [null, [Validators.required, verifyDate]],
        departureTime: [null, [Validators.required, verifyTime]],
        departureAddress: [null, Validators.required],
        price: [null as number | null, [Validators.required, Validators.min(0.5)]],
    });

    // Método para inicializar el componente y cargar los datos necesarios
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

    // Variable para controlar si el formulario ha sido enviado
    submited = false;

    // Método para manejar el envío del formulario de publicación de viajes
    submit() {
        this.submited = true;
        if (this.form.invalid || this.loading) return;
        console.log(this.form.value);
        
        console.log(this.form.invalid);
        
        this.loading = true;
        this.error = '';
        const v = this.form.value;
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
            }))
            .subscribe({
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
}
